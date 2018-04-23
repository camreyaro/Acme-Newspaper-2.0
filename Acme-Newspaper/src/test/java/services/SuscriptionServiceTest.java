
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Newspaper;
import domain.Suscription;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SuscriptionServiceTest extends AbstractTest {

	// The SUT -------------------------------------

	@Autowired
	private SuscriptionService	suscriptionService;
	@Autowired
	private NewspaperService	newspaperService;
	private Object				oops;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				"customer2", "newspaper3", null, "suscribe"
			}, //Suscribirse a un periodico exitosamente

			{
				"customer1", "newspaper3", IllegalArgumentException.class, "suscribe"
			}, //Suscribirse a un periodico, en el que ya estas suscrito

			{
				"customer1", "newspaper1", IllegalArgumentException.class, "suscribe"
			}, //Suscribirse a un periodico publico

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.template((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------

	protected void template(final String username, final String newspaperTitle, final Class<?> expected, final String operation) {
		Class<?> caught;

		caught = null;
		try {
			Newspaper newspaper;

			super.authenticate(username);
			final Suscription suscription = this.suscriptionService.create();
			final CreditCard creditCard = new CreditCard();
			creditCard.setBrandName("BrandName");
			creditCard.setCvvCode(789);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2020);
			creditCard.setHolderName("Holder Name");
			creditCard.setNumber("1234567891234567");

			suscription.setCreditCard(creditCard);

			newspaper = this.newspaperService.findOne(this.getEntityId(newspaperTitle));
			suscription.setNewspaper(newspaper);
			this.suscriptionService.suscribe(suscription);

			this.suscriptionService.saveAndFlush(suscription);
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
