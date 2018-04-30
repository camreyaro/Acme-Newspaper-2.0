
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.SuscriptionVolumen;
import domain.Volumen;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SuscriptionVolumenServiceTest extends AbstractTest {

	@Autowired
	private SuscriptionVolumenService	suscriptionVolumenService;
	@Autowired
	private VolumenService				volumenService;


	@Test
	public void sampleDriver() {
		final Object testingData[][] = {

			{
				true, "volumen1", "customer2", null
			}, // Crear suscripcion
			{
				false, "volumen1", "customer2", IllegalArgumentException.class
			}, // Crear suscripcion (cc invalid)
			{
				true, "volumen2", "customer2", IllegalArgumentException.class
			}, // Crear suscripcion (ya existente)

		};

		for (int i = 0; i < testingData.length; i++) {

			super.startTransaction();
			this.template((Boolean) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
		}
	}
	protected void template(final Boolean valid, final String volumenId, final String customerId, final Class<?> expected) {
		Class<?> caught;
		final CreditCard creditCard;

		final Volumen v;

		caught = null;
		try {
			if (valid) {
				creditCard = new CreditCard();
				creditCard.setBrandName("BrandName");
				creditCard.setCvvCode(789);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2020);
				creditCard.setHolderName("Holder Name");
				creditCard.setNumber("1234567891234567");
			} else {
				creditCard = new CreditCard();
				creditCard.setBrandName("BrandName");
				creditCard.setCvvCode(789);
				creditCard.setExpirationMonth(12);
				creditCard.setExpirationYear(2002); //cc no valida
				creditCard.setHolderName("Holder Name");
				creditCard.setNumber("1234567891234567");
			}

			super.authenticate(customerId);
			v = this.volumenService.findOne(this.getEntityId(volumenId));
			final SuscriptionVolumen sv = this.suscriptionVolumenService.create(v);
			sv.setCreditCard(creditCard);
			this.suscriptionVolumenService.save(sv);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}
		this.checkExceptions(expected, caught);
	}
}
