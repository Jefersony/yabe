package controllers;

import model.CarroModel;
import play.mvc.Controller;

public class TesteController extends Controller{
	
	public static void servicoTeste() {
		CarroModel carro = new CarroModel();
		
		carro.idCarro = 2;
		carro.nomeCarro = "Uno";
		renderJSON(carro);
	}

}
