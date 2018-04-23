
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Article;
import domain.FollowUp;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FollowUpServiceTest extends AbstractTest {

	// --- The SUT --- 
	@Autowired
	private ArticleService	articleService;

	@Autowired
	private FollowUpService	followUpService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			// --- CREATE & SAVE

			{
				"user3", "title", "text", null, "article9", null, null, "createAndSave"// Crearlo normal y correcto.
			}, {
				"user3", null, "text", null, "article9", null, IllegalArgumentException.class, "createAndSave" // No se puede guardar sin título.
			}, {
				"user3", "title", null, null, "article9", null, IllegalArgumentException.class, "createAndSave" // No se puede guardar sin texto.
			}, {
				"user3", "title", "text", null, null, null, IllegalArgumentException.class, "createAndSave" //No se puede crear sin artículo.
			}

			, {
				"user2", "title", "text", null, "article9", null, IllegalArgumentException.class, "createAndSave" //No puedo crear uno que no es mío.
			}, {
				"user4", "title", "text", null, "article5", null, IllegalArgumentException.class, "createAndSave" //No puedo crear FollowUps de un artículo no terminado.
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6], (String) testingData[i][7]);

	}
	// Método base para realizar test

	protected void template(final String username, final String title, final String text, final String pictureURL, final String articleName, final String followUpName, final Class<?> expected, final String operation) {
		Class<?> caught;
		FollowUp followUp;

		caught = null;
		try {
			super.unauthenticate();
			if (operation.equals("re-save")) {
				super.authenticate(username);
				followUp = this.followUpService.findOne(this.getEntityId(followUpName));
				this.followUpService.save(followUp);
			} else if (operation.equals("createAndSave")) {
				super.authenticate(username);
				Article a = null;
				if (articleName != null)
					a = this.articleService.findOne(this.getEntityId(articleName));

				followUp = this.followUpService.create(a);

				followUp.setTitle(title);
				followUp.setText(text);

				this.followUpService.save(followUp);

			} else if (operation.equals("createAndSaveNonAuth")) {
				final Article a = this.articleService.findOne(this.getEntityId(articleName));

				followUp = this.followUpService.create(a);
				followUp.setTitle(title);
				followUp.setText(text);

				this.followUpService.save(followUp);

			}

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
