package de.hsrm.mi.web.bratenbank.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerRepository;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerService;
import de.hsrm.mi.web.bratenbank.benutzerapi.BenutzerRestApi;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb07_A2_BenutzerRestApi {
	private static final String TESTLOGINNAME = "alap";
	private static final String TESTLOGINPASSWORT = "aaa";
	private static final String TESTLOGINVOLLNAME = "Alfred Appel";
	private static final boolean TESTLOGINNUTZUNGSBEDINGUNGENOK = true;

	private static final String TESTLOGINNAME2 = "blapf";
	private static final String TESTLOGINPASSWORT2 = "blubber";
	private static final String TESTLOGINVOLLNAME2 = "Berta Benzel";
	private static final boolean TESTLOGINNUTZUNGSBEDINGUNGENOK2 = true;

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	BenutzerService benutzerservice;

	@Autowired
	BenutzerRepository benutzerrepo;

	@Autowired
	BenutzerRestApi benutzerrestapi;

	private static String TESTLOGINJSON;

	@Test
	void vorabcheck() {
		assertThat(benutzerrestapi).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@BeforeAll
	public static void initAll() {
		/* JSON sieht so aus:
		{
		  	"vollname":  "Alfred Appel",
  		  	"loginname": "alap",
  			"passwort":  "aaa",
  			"nutzungsbedingungenok": "true"
		}
		*/
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("vollname", TESTLOGINVOLLNAME);
		json.put("loginname", TESTLOGINNAME);
		json.put("passwort", TESTLOGINPASSWORT);
		json.put("nutzungsbedingungenok", TESTLOGINNUTZUNGSBEDINGUNGENOK);
		TESTLOGINJSON = json.toString();
	}

	@BeforeEach
	public void init() {
		benutzerrepo.deleteAll();
	}

	@Test
	@DisplayName("POST /api/benutzer legt neuen Benutzer an")
	void postBenutzer() throws Exception {
		mockmvc.perform(
		post("/api/benutzer/")
			.contentType("application/json")
			.content(TESTLOGINJSON)
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.loginname").value(TESTLOGINNAME))
		.andExpect(jsonPath("$.vollname").value(TESTLOGINVOLLNAME))
		.andExpect(jsonPath("$.nutzungsbedingungenok").value(TESTLOGINNUTZUNGSBEDINGUNGENOK))
		;
	}


	@Test
	@DisplayName("POST /api/benutzer legt selben Benutzer zweimal an - falsch")
	void postDoppelBenutzer() throws Exception {
		mockmvc.perform(
			post("/api/benutzer")
				.contentType("application/json")
				.content(TESTLOGINJSON)
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.loginname").value(TESTLOGINNAME))
		.andExpect(jsonPath("$.vollname").value(TESTLOGINVOLLNAME))
		.andExpect(jsonPath("$.nutzungsbedingungenok").value(TESTLOGINNUTZUNGSBEDINGUNGENOK))
		;

		assertThat(benutzerrepo.findByLoginname(TESTLOGINNAME)).isNotNull();

		mockmvc.perform(
			post("/api/benutzer")
				.contentType("application/json")
				.content(TESTLOGINJSON)
		)
		.andExpect(status().isBadRequest())
		;
	}


	@Test
	@DisplayName("GET /api/benutzer/loginname fragt Benutzer ab")
	void getBenutzerById() throws Exception {
		// Benutzer anlegen
		Benutzer neubenutzer = new Benutzer();
		neubenutzer.setVollname(TESTLOGINVOLLNAME2);
		neubenutzer.setLoginname(TESTLOGINNAME2);
		neubenutzer.setPasswort(TESTLOGINPASSWORT2);
		neubenutzer.setNutzungsbedingungenok(TESTLOGINNUTZUNGSBEDINGUNGENOK2);
		
		// Benutzer in DB speichern
		benutzerrepo.save(neubenutzer);

		// Gucken, ob der Service ihn auch sieht
		assertThat(benutzerservice.findeBenutzer(TESTLOGINNAME2)).isNotNull();

		// Benutzer über REST abfragen
		mockmvc.perform(
			get("/api/benutzer/"+TESTLOGINNAME2)
				.contentType("application/json")
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.loginname").value(TESTLOGINNAME2))
		.andExpect(jsonPath("$.vollname").value(TESTLOGINVOLLNAME2))
		.andExpect(jsonPath("$.nutzungsbedingungenok").value(TESTLOGINNUTZUNGSBEDINGUNGENOK2))
		;
	}


	@Test
	@DisplayName("GET /api/benutzer/loginname fragt nichtexistierenden Benutzer ab")
	void getNonexBenutzerById() throws Exception {
		// Benutzer2 anlegen
		Benutzer neubenutzer = new Benutzer();
		neubenutzer.setVollname(TESTLOGINVOLLNAME2);
		neubenutzer.setLoginname(TESTLOGINNAME2);
		neubenutzer.setPasswort(TESTLOGINPASSWORT2);
		neubenutzer.setNutzungsbedingungenok(true);
		
		// Benutzer2 in DB speichern
		benutzerrepo.save(neubenutzer);

		// Benutzer (nicht Benutzer2) über REST abfragen, sollte nicht da sein
		mockmvc.perform(
			get("/api/benutzer/"+TESTLOGINNAME)
				.contentType("application/json")
		)
		.andExpect(status().isBadRequest());
		;
	}

}
