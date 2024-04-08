import { Component, OnInit } from '@angular/core';
import { RutinasService } from './rutina.service';
import { NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioRutinaComponent} from './formulario-rutina/formulario-rutina.component'
import { Rutina } from './rutina';
import { Ejercicio } from './ejercicio';
import { FormularioEjercicioComponent } from './formulario-ejercicio/formulario-ejercicio.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  ejercicios: Ejercicio [] = [];
  rutinas: Rutina [] = [];
  ejercicioElegido?: Ejercicio;
  rutinaElegida?: Rutina;

  constructor(private rutinaService: RutinasService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.rutinaService.getRutinas().subscribe(rutinas => {
      this.rutinas = rutinas;
    });
    this.rutinaService.getEjercicios().subscribe(ejercicios => {
      this.ejercicios = ejercicios;
    });
  }


  elegirRutina(rutina: Rutina): void {
    this.rutinaElegida = rutina;
  }

  aniadirRutina(): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.rutina = {id: 0, nombre: '', ejercicios: [], descripcion: ''};
    ref.result.then((rutina: Rutina) => {
      this.rutinaService.addRutina(rutina);
      this.rutinaService.getRutinas().subscribe(rutinas => {
        this.rutinas = rutinas;
      });
    }, (reason) => {});

  }

  rutinaEditada(rutina: Rutina): void {
    this.rutinaService.editarRutina(rutina);
    this.rutinaService.getRutinas().subscribe(rutinas => {
      this.rutinas = rutinas;
      this.rutinaElegida = this.rutinas.find(c => c.id == rutina.id);
    });
  }

  eliminarRutina(id: number): void {
    this.rutinaService.eliminarRutina(id);
    this.rutinaService.getRutinas().subscribe(rutinas => {
      this.rutinas = rutinas;
      this.rutinaElegida = undefined;
    });
  }

  elegirEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioElegido = ejercicio;
  }

  aniadirEjercicio(): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.ejercicio = {id: 0, nombre: '',observaciones: '',tipo: '',musculosTrabajados: '',material: '',dificultad: '', descripcion: '', video: '', foto: ''};
    ref.result.then((ejercicio: Ejercicio) => {
      this.ejercicios.push(ejercicio);
    }, (reason) => {});
  }

  ejercicioEditado(ejercicio: Ejercicio): void {
    this.rutinaService.editarEjercicio(ejercicio);
    this.rutinaService.getEjercicios().subscribe(ejercicios => {
      this.ejercicios = ejercicios;
      this.ejercicioElegido = this.ejercicios.find(c => c.nombre == ejercicio.nombre);
    });
  }

  eliminarEjercicio(id: number): void {
    this.rutinaService.eliminarEjercicio(id);
    this.rutinaService.getEjercicios().subscribe(ejercicios => {
      this.ejercicios = ejercicios;
      this.ejercicioElegido = undefined;
    });
  }


}
