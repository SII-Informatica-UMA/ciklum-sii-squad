import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Ejercicio_rutina } from '../entities/ejercicio_rutina';


@Component({
  selector: 'app-formulario-rutina-ejercicio',
  templateUrl: './formulario-rutina-ejercicio.component.html',
  styleUrls: ['./formulario-rutina-ejercicio.component.css']
})
export class FormularioRutinaEjercicioComponent {
  
  ejercicioRutina: Ejercicio_rutina = {series: 1,repeticiones: 1,duracionMinutos: 1,ejercicio: {id: 1,nombre: '',descripcion: '',observaciones: '',tipo: '',musculosTrabajados: '',material: '',dificultad: '',multimedia: []}
  };

  /**
   * @param modal referencia a model actual abierto
   */
  constructor (public modal: NgbActiveModal){}
  
  
  guardarEjercicioRutina(){
    this.modal.close(this.ejercicioRutina);
  }
}
