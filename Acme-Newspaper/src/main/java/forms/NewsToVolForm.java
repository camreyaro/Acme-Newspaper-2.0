
package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Newspaper;
import domain.Volumen;

public class NewsToVolForm {

	private Newspaper	newspaper;
	private Volumen		volumen;


	@Valid
	@NotNull
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}
	@Valid
	@NotNull
	public Volumen getVolumen() {
		return this.volumen;
	}

	public void setVolumen(final Volumen volumen) {
		this.volumen = volumen;
	}

}
