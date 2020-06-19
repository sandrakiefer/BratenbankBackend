package de.hsrm.mi.web.bratenbank.test.ueb07;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import de.hsrm.mi.web.bratenbank.benutzer.Benutzer;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerRepository;
import de.hsrm.mi.web.bratenbank.benutzer.BenutzerService;
import de.hsrm.mi.web.bratenbank.bratapi.BratenRestApi;
import de.hsrm.mi.web.bratenbank.bratrepo.Braten;
import de.hsrm.mi.web.bratenbank.bratrepo.BratenRepository;
import de.hsrm.mi.web.bratenbank.bratservice.BratenService;

/*
 * Bitte führen Sie den Test auf der Kommandozeile mit
 * 
 *   ./gradlew clean test
 * 
 * aus. Stellen Sie sicher, dass die Datei 
 * 
 *   src/test/properties/application.properties
 * 
 * aus dem Test-Zip in Ihrem Projekt ist. Diese zweite application.properties
 * stellt sicher, dass gegen die "in-memory"-Konfiguration der H2-Datenbank
 * getestet wird, die bei jedem Anwendungsstart leer ist. Keine Reste aus
 * früheren Läufen in der DB zu haben ist zum Testen hilfreich. Dies
 * betrifft *nicht* Ihre src/java/resources/application.properties, in der
 * Sie Ihre Datenbank zum Entwickeln vielleicht anders konfiguriert haben.
 * 
 * Wenn der VSCode-Testrunner Ihnen (insb. datenbankbezogene) Fehler liefert,
 * kann das daran hängen, dass die Test-application.properties nicht korrekt
 * berücksichtigt wird. Testen Sie daher zunächst über das o.g. Kommando.
 * Falls Sie den VSCode Testrunner verwenden wollen, können Sie das tun, indem
 * Sie in Ihrer src/java/resources/application.properties die H2-JDBC-URL
 * auf "in-memory"-Betrieb umstellen (also 
 * 
 *     spring.datasource.url=jdbc:h2:mem:bratenbank
 * 
 * statt ...h2:~/bratenbank.
 */ 

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class Web_Ueb07_A4_BratenRestApi {
	private static final String TESTLOGINNAME = "spacewubbo";
	private static final String TESTLOGINPASSWORT = "hoiallemaal";
	private static final String TESTLOGINVOLLNAME = "Wubbo van Ruimtevaart";

	private static final String TESTLOGINNAMEFALSCH = "blubbo";
	
	static final String[][] ALLEBRATENDATEN = { 
			{ "Astweg 1, 52070 Aachen", "Ameisenbraten mit Ahornsirup", "25" },
			{ "Bommel Berg 25, 10115 Berlin", "Burgerbratling mit Birne", "100" },
			{ "Chemieplatz 456, 09117 Chemnitz", "Chemiebraten mit BASF", "75" },
			{ "In den Blamuesen 1234, 40489 Duesseldorf bei Krefeld", "Dodohackbraten in Pfeffersosse", "0" }, };

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	BratenService bratenservice;

	@Autowired
	BratenRepository bratenrepo;

	@Autowired
	BratenRestApi bratenrestapi;

	@Autowired
	BenutzerRepository benutzerrepo;

	@Autowired
	BenutzerService benutzerservice;

	@Test
	void vorabcheck() {
		assertThat(bratenrestapi).isNotNull();
		assertThat(mockmvc).isNotNull();
	}

	@BeforeAll
	public static void initAll() {
	}

	@BeforeEach
	@Transactional
	public void init() {
		bratenrepo.deleteAll();
		benutzerrepo.deleteAll();

		Benutzer neubenutzer = new Benutzer();
		neubenutzer.setLoginname(TESTLOGINNAME);
		neubenutzer.setVollname(TESTLOGINVOLLNAME);
		neubenutzer.setPasswort(TESTLOGINPASSWORT);
		neubenutzer.setNutzungsbedingungenok(true);
		Benutzer ben = benutzerrepo.save(neubenutzer);

		for (String[] a: ALLEBRATENDATEN) {
			Braten b = new Braten();
			b.setAbholort(a[0]);
			b.setHaltbarbis(LocalDate.now().plusDays(17));
			b.setBeschreibung(a[1]);
			b.setVgrad(Integer.valueOf(a[2]));
			b.setAnbieter(ben);
			Braten mb = bratenrepo.save(b);
			ben.getAngebote().add(mb);
		}

	}



	@Test
	@DisplayName("GET /api/braten fragt Liste aller Braten ab")
	void getAlleBraten() throws Exception {
		mockmvc.perform(
			get("/api/braten")
				.contentType("application/json")
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$", hasSize(ALLEBRATENDATEN.length)))
		.andExpect(jsonPath("$[*].abholort", 
			containsInAnyOrder(ALLEBRATENDATEN[0][0], ALLEBRATENDATEN[1][0], 
								ALLEBRATENDATEN[2][0], ALLEBRATENDATEN[3][0])))
		.andExpect(jsonPath("$[*].beschreibung", 
			containsInAnyOrder(ALLEBRATENDATEN[0][1], ALLEBRATENDATEN[1][1], 
							ALLEBRATENDATEN[2][1], ALLEBRATENDATEN[3][1])))
		.andExpect(jsonPath("$[0].anbieter.loginname", is(TESTLOGINNAME)))
		.andExpect(jsonPath("$[1].anbieter.loginname", is(TESTLOGINNAME)))
		.andExpect(jsonPath("$[2].anbieter.loginname", is(TESTLOGINNAME)))
		.andExpect(jsonPath("$[3].anbieter.loginname", is(TESTLOGINNAME)))
		;
	}



	@Test
	@DisplayName("GET /api/braten/id fragt bestimmten Braten ab")
	void getBratenById() throws Exception {
		for (Braten testbraten: bratenrepo.findAll()) {
			mockmvc.perform(
				get("/api/braten/"+testbraten.getId())
					.contentType("application/json")
			)
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.abholort", is(testbraten.getAbholort())))
			.andExpect(jsonPath("$.beschreibung", is(testbraten.getBeschreibung())))
			.andExpect(jsonPath("$.vgrad", is(testbraten.getVgrad())))
			.andExpect(jsonPath("$.id", is(testbraten.getId())))
			.andExpect(jsonPath("$.version", is(testbraten.getVersion())))
			.andExpect(jsonPath("$.anbieter.loginname", is(testbraten.getAnbieter().getLoginname())))
			.andExpect(jsonPath("$.anbieter.vollname",  is(testbraten.getAnbieter().getVollname())))
			;
		}
	}



	@Test
	@DisplayName("GET /api/braten/id fragt bestimmten Braten mit falscher ID ab")
	void getBratenByPfaltscherId() throws Exception {
		Optional<Braten> maxbraten = bratenrepo.findAll().stream().max(Comparator.comparing(Braten::getId));
		int pfaltschid = maxbraten.get().getId() + 17;

		mockmvc.perform(
				get("/api/braten/"+pfaltschid)
					.contentType("application/json")
		)
		.andExpect(status().is4xxClientError())
		;
	}



	@Test
	@DisplayName("DELETE /api/braten/id löscht bestimmten Braten")
	void deleteBratenById() throws Exception {
		assertThat(bratenrepo.count()).isGreaterThan(3);

		List<Braten> allebraten = bratenrepo.findAll();

		Braten b3 = allebraten.get(3);
		mockmvc.perform(
				delete("/api/braten/"+b3.getId())
					.contentType("application/json")
		)
		.andExpect(status().is2xxSuccessful());
		;
		assertThat(bratenrepo.findById(b3.getId()).isPresent()).isFalse();


		Braten b2 = allebraten.get(2);
		mockmvc.perform(
				delete("/api/braten/"+b2.getId())
					.contentType("application/json")
		)
		.andExpect(status().is2xxSuccessful());
		;
		assertThat(bratenrepo.findById(b2.getId()).isPresent()).isFalse();

	}



	@Test
	@DisplayName("DELETE /api/braten/id mit nicht-existierender ID")
	void deleteBratenByPfaltscherId() throws Exception {
		Optional<Braten> maxbraten = bratenrepo.findAll().stream().max(Comparator.comparing(Braten::getId));
		int pfaltschid = maxbraten.get().getId() + 17;

		mockmvc.perform(
				delete("/api/braten/"+pfaltschid)
					.contentType("application/json")
		)
		.andExpect(status().is4xxClientError());
		;
	}


	@Test
	@DisplayName("POST /api/braten legt neuen Braten für übergebenen Nutzer an")
	void postBraten() throws Exception {
		/* Beispiel JSON:
			{
				"abholort":    "Am Zipfel 1, 12345 Vollradisroda",
				"haltbarbis":  "2022-11-17",
				"beschreibung":"Super leckerer Bratwurstbraten",
				"vgrad": 25
			}

		*/
		String TESTABHOLORT = "Am Zipfel 1, 12345 Vollradisroda";
		String TESTHALTBARBIS = "2022-11-17";
		String TESTBESCHREIBUNG = "Super leckerer Bratwurstbraten";
		int TESTVGRAD = 25;

		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("abholort", TESTABHOLORT);
		json.put("haltbarbis", TESTHALTBARBIS);
		json.put("beschreibung", TESTBESCHREIBUNG);
		json.put("vgrad", TESTVGRAD);
		String testbratenjson = json.toString();

		
		mockmvc.perform(
				post("/api/braten?loginname="+TESTLOGINNAME)
					.contentType("application/json")
					.content(testbratenjson)
		)
		.andExpect(status().is2xxSuccessful())
		.andExpect(jsonPath("$.abholort").value(TESTABHOLORT))
		.andExpect(jsonPath("$.haltbarbis").value(TESTHALTBARBIS))
		.andExpect(jsonPath("$.beschreibung").value(TESTBESCHREIBUNG))
		.andExpect(jsonPath("$.vgrad").value(TESTVGRAD))
		.andExpect(jsonPath("$.anbieter.loginname").value(TESTLOGINNAME))
		.andExpect(jsonPath("$.anbieter.vollname").value(TESTLOGINVOLLNAME))
		;

		assertThat(bratenrepo.findByBeschreibungContainsIgnoringCase(TESTBESCHREIBUNG).size()).isEqualTo(1);
	}



	@Test
	@DisplayName("POST /api/braten mit unbekanntem Nutzer")
	void postBratenNutzerLeiderPfaltsch() throws Exception {
		String TESTABHOLORT = "Am Zipfel 1, 12345 Vollradisroda";
		String TESTHALTBARBIS = "2022-11-17";
		String TESTBESCHREIBUNG = "Super leckerer Bratwurstbraten";
		int TESTVGRAD = 25;

		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("abholort", TESTABHOLORT);
		json.put("haltbarbis", TESTHALTBARBIS);
		json.put("beschreibung", TESTBESCHREIBUNG);
		json.put("vgrad", TESTVGRAD);
		String testbratenjson = json.toString();

		long vorher = bratenrepo.count();
		
		mockmvc.perform(
				post("/api/braten?loginname="+TESTLOGINNAMEFALSCH)
					.contentType("application/json")
					.content(testbratenjson)
		)
		.andExpect(status().is4xxClientError());
		;

		long nachher = bratenrepo.count();
		assertThat(vorher).isEqualTo(nachher);
	}



	@Test
	@DisplayName("POST /api/braten mit nicht-validen Braten (Abholort)")
	void postFehlBratenAbholort() throws Exception {
		String TESTABHOLORTFEHLERHAFT = "Ohne Hausnr, 1 Ort";
		String TESTHALTBARBIS = "2022-11-17";
		String TESTBESCHREIBUNG = "Super leckerer Bratwurstbraten";
		int TESTVGRAD = 25;

		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("abholort", TESTABHOLORTFEHLERHAFT);
		json.put("haltbarbis", TESTHALTBARBIS);
		json.put("beschreibung", TESTBESCHREIBUNG);
		json.put("vgrad", TESTVGRAD);
		
		mockmvc.perform(
				post("/api/braten?loginname="+TESTLOGINNAME)
					.contentType("application/json")
					.content(json.toString())
		)
		.andExpect(status().is4xxClientError())
		;
	}

	@Test
	@DisplayName("POST /api/braten mit gesundheitlich bedenklichem Haltbarkeitsdatum")
	void postFehlBratenHaltbarbis() throws Exception {
		String TESTABHOLORT = "Am Zipfel 1, 12345 Vollradisroda";
		String TESTHALTBARBISFEHLERHAFT = "1825-11-17";
		String TESTBESCHREIBUNG = "Super leckerer Bratwurstbraten";
		int TESTVGRAD = 25;

		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("abholort", TESTABHOLORT);
		json.put("haltbarbis", TESTHALTBARBISFEHLERHAFT);
		json.put("beschreibung", TESTBESCHREIBUNG);
		json.put("vgrad", TESTVGRAD);
		
		mockmvc.perform(
				post("/api/braten?loginname="+TESTLOGINNAME)
					.contentType("application/json")
					.content(json.toString())
		)
		.andExpect(status().is4xxClientError())
		;
	}


}
