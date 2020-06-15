package de.hsrm.mi.web.bratenbank.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import de.hsrm.mi.web.bratenbank.bratrepo.BratenRepository;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class Web_Ueb07_A3_BenutzerRestApiAttrVerstecken {
	private static final String TESTLOGINNAME = "alap";
	private static final String TESTLOGINPASSWORT = "aaa";
	private static final String TESTLOGINVOLLNAME = "Alfred Appel";
	private static final String TESTLOGINNUTZUNGSBEDINGUNGENOK = "true";

	private static final String TESTLOGINNAME2 = "blapf";
	private static final String TESTLOGINPASSWORT2 = "blubber";
	private static final String TESTLOGINVOLLNAME2 = "Berta Benzel";

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	BenutzerService benutzerservice;

	@Autowired
	BratenRepository bratenrepo;

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
		System.out.println("init #anazhl bratenrepo " + bratenrepo.count());
		bratenrepo.deleteAll();
		System.out.println("init #anazhl benutzerrepo " + benutzerrepo.count());
		benutzerrepo.deleteAll();
	}

	@Test
	@DisplayName("POST /api/benutzer legt neuen Benutzer (incl. Passwort) an, Serialisierung enthält verbotene Felder nicht")
	@Transactional
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
		// ... und die drei sind *alle* Felder im Rückgabe-JSON
		.andExpect(jsonPath("$.*", hasSize(3)))
		// ... mit anderen Worten die folgenden sollten in der JSON-Serialisierung nicht auftauchen
		.andExpect(jsonPath("$.angebote").doesNotExist())
		.andExpect(jsonPath("$.passwort").doesNotExist())
		.andExpect(jsonPath("$.id").doesNotExist())
		.andExpect(jsonPath("$.version").doesNotExist())
		;

		// ... trotzdem muss Passwort in DB gesetzt worden sein (JSON-Deserialisierung bei POST)
		Benutzer b = benutzerrepo.findByLoginname(TESTLOGINNAME);
		assertThat(b.getPasswort()).isEqualTo(TESTLOGINPASSWORT);
	}

	@Test
	@DisplayName("GET /api/benutzer/loginname für ex. Benutzer enthält auch keine verbotenen Felder")
	@Transactional
	void getBenutzerById() throws Exception {
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
			get("/api/benutzer/"+TESTLOGINNAME2)
				.contentType("application/json")
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.loginname").value(TESTLOGINNAME2))
		.andExpect(jsonPath("$.vollname").value(TESTLOGINVOLLNAME2))
		.andExpect(jsonPath("$.nutzungsbedingungenok").value(true))
		// ... und die drei sind *alle* Felder im Rückgabe-JSON
		.andExpect(jsonPath("$.*", hasSize(3)))
		// ... mit anderen Worten die folgenden sollten in der JSON-Serialisierung nicht auftauchen
		.andExpect(jsonPath("$.angebote").doesNotExist())
		.andExpect(jsonPath("$.passwort").doesNotExist())
		.andExpect(jsonPath("$.id").doesNotExist())
		.andExpect(jsonPath("$.version").doesNotExist())

		
		;
	}


}
