import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { Rutina } from '../entities/rutina';
import { Ejercicio } from '../entities/ejercicio'
import { EjercicioService } from '../services/ejercicio.service'
import { Ejercicio_rutina } from '../entities/ejercicio_rutina';
import { FormularioRutinaEjercicioComponent } from '../formulario-rutina-ejercicio/formulario-rutina-ejercicio.component';

@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion!: "Añadir" | "Editar";
  rutina: Rutina = {id: 0, nombre: '', ejercicios: [], descripcion: '',observaciones: ''};
  ejercicios: Ejercicio_rutina[] = []; //Lista todos los ejercicios. Usado solo en el constructor

  constructor(public modal: NgbActiveModal, private ejercicioService: EjercicioService, private modalService: NgbModal) {
    this.ejercicioService.getEjercicios().subscribe(result => {
      for (let i = 0; i < result.length; i++) {
        this.ejercicios.push({series:0,repeticiones:0,duracionMinutos:0,ejercicio: result[i]})
      }
    });
  }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }
/**
 * Si esta en rutina lo elimina, y sino, lo añade
 * @param ejercicio 
 */
  modificaEjercicio(ejercicio: Ejercicio_rutina): void {
    let idx = this.rutina.ejercicios.findIndex(x => x.ejercicio.id === ejercicio.ejercicio.id);
    if(idx === -1){
      //Añadir ejercicio
      this.rutina.ejercicios.push(ejercicio);

    }else{
      //Eliminar ejercicio
      this.rutina.ejercicios.splice(idx,1);
    }
  }

  buscaEjercicio(ejercicio: Ejercicio_rutina): boolean {
    var res: boolean = this.rutina.ejercicios.find(x => x.ejercicio.id === ejercicio.ejercicio.id) !== undefined;
    return res;
  }

  ejercicioRutina(ejercicio: Ejercicio_rutina): void{
    let ref = this.modalService.open(FormularioRutinaEjercicioComponent);
    let idx = this.ejercicios.findIndex(x => x.ejercicio.id === ejercicio.ejercicio.id);
    ref.componentInstance.ejercicioRutina = {...this.rutina.ejercicios[idx]};
    ref.result.then((ejercicio: Ejercicio_rutina) => {     
        this.rutina.ejercicios[idx] = ejercicio;
    }, (reason) => {});
  }
}
