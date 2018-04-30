
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Newspaper;
import domain.Volumen;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VolumenServiceTest extends AbstractTest {

	@Autowired
	private VolumenService		volumenService;
	@Autowired
	private NewspaperService	newspaperService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"user1", "hola", "buenas tardes", null, "save", null, null
			}, //Crear un article (positivo del save)
			{
				"user1", null, "buenas tardes", IllegalArgumentException.class, "save", null, null
			}, //No puedes crearlo sin nombre
			{
				"user1", "hola", "buenas tardes", IllegalArgumentException.class, "save", "volumen1", null
			}, //Los volumenes solo se pueden crear no editar (negativo del save)
			{
				"user1", "hola", "buenas tardes", null, "add", "volumen1", "newspaper2"
			}, // Se le puede añadir un periodico
			{
				"user1", "hola", "buenas tardes", IllegalArgumentException.class, "add", "volumen1", "newspaper8"
			},// No se le puede añadir un periódico que ya tenga
			{
				"user1", "hola", "buenas tardes", IllegalArgumentException.class, "add", "volumen1", "newspaper1"
			},//No se puede añadir un periodico no publicado
			{
				"user1", "hola", "buenas tardes", IllegalArgumentException.class, "remove", "volumen1", "newspaper2"
			}, // No tiene ese periodico (error)
			{
				"user2", "hola", "buenas tardes", IllegalArgumentException.class, "remove", "volumen1", "newspaper8"
			},// No eres el dueño de ese volumen
			{
				"user1", "hola", "buenas tardes", null, "remove", "volumen1", "newspaper8"
			}
		//Borrar periodico
		};

		for (int i = 0; i < testingData.length; i++) {

			super.startTransaction();
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6]);
		}
	}

	protected void template(final String username, final String vTitle, final String vDescription, final Class<?> expected, final String operation, final String volumenId, final String newspaperId) {
		Class<?> caught;
		Volumen v;
		final Newspaper n;

		caught = null;
		try {
			if (operation.equals("save")) {
				super.authenticate(username);
				if (volumenId == null)
					v = this.volumenService.create();
				else
					v = this.volumenService.findOne(this.getEntityId(volumenId));
				v.setTitle(vTitle);
				v.setDescription(vDescription);
				this.volumenService.save(v);
				super.unauthenticate();
			} else if (operation.equals("add")) {
				v = this.volumenService.findOne(this.getEntityId(volumenId));
				n = this.newspaperService.findOne(this.getEntityId(newspaperId));
				this.volumenService.addNewspaper(v, n);
			} else {
				v = this.volumenService.findOne(this.getEntityId(volumenId));
				n = this.newspaperService.findOne(this.getEntityId(newspaperId));
				this.volumenService.removeNewspaper(v, n);
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		this.checkExceptions(expected, caught);
	}
}
