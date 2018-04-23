
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Newspaper;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class NewspaperServiceTest extends AbstractTest {

	@Autowired
	private NewspaperService	newspaperService;


	@Test
	public void adminDriver() {
		final Object testingData[][] = {

			{
				"admin", "newspaper1", null, "delete" // Borrado correcto
			},

			{
				"user1", "newspaper1", IllegalArgumentException.class, "delete" // Un user no puede borrar un artículo
			},

			{
				"admin", "newspaper1", null, "spamNewspapers" // Listar artículos con spam
			},

			{
				"user1", "newspaper5", IllegalArgumentException.class, "owner" //un usuario no puede editar el newspaper de otro
			},

			{
				"user1", "newspaper1", null, "save" //un usuario si puede editar su newspaper
			}, {
				"user1", "newspaper1", IllegalArgumentException.class, "publish", //un usuario no puede publicar un newspaper sin todos los articulos guardados
			}, {
				"user1", "newspaper1", null, "saveName", //un usuario puede editar un newspaper con titulo
			}, {
				"user1", "newspaper1", ConstraintViolationException.class, "saveNullName", //un usuario no puede editar un newspaper con titulo nulo
			}, {
				"user1", "newspaper1", null, "privateWprice", //un usuario puede guardar un newspaper privado con precio
			}, {
				"user1", "newspaper1", IllegalArgumentException.class, "privateWoutprice", //un usuario puede guardar un newspaper privado sin precio
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.templateAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}

	}
	// Método base para realizar test

	protected void templateAdmin(final String username, final String articleName, final Class<?> expected, String operation) {
		Class<?> caught;
		Newspaper newspaper;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.findOne(super.getEntityId(articleName));

			if (operation.equals("delete"))
				this.newspaperService.delete(newspaper);
			else if (operation.equals("owner"))
				this.newspaperService.save(newspaper);
			else if (operation.equals("save"))
				this.newspaperService.save(newspaper);
			else if (operation.equals("saveName")) {
				newspaper.setTitle("Test name");
				this.newspaperService.saveAndFlush(newspaper);
			} else if (operation.equals("saveNullName")) {
				newspaper.setTitle(null);
				this.newspaperService.saveAndFlush(newspaper);
			} else if (operation.equals("privateWprice")) {
				newspaper.setPublicNp(false);
				newspaper.setPrice(10.0);
				this.newspaperService.saveAndFlush(newspaper);
			} else if (operation.equals("privateWoutprice")) {
				newspaper.setPublicNp(false);
				newspaper.setPrice(0.0);
				this.newspaperService.save(newspaper);
			} else if (operation.equals("publish"))
				this.newspaperService.publish(newspaper);
			else if (operation.equals("spamArticles")) {
				newspaper.setDescription("viagra");
				this.newspaperService.save(newspaper);
				Assert.isTrue(this.newspaperService.getNewspapersWithSpamWords().contains(newspaper));
			}

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
