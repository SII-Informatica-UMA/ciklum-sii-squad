import { Component, Input, Output, EventEmitter } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioRutinaComponent} from '../formulario-rutina/formulario-rutina.component'
import {FormularioEjercicioComponent} from '../formulario-ejercicio/formulario-ejercicio.component'
import { RutinasService } from '../rutina.service';
import { Rutina } from '../rutina';
import { Ejercicio } from '../ejercicio';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-detalle-ejercicio',
  templateUrl: './detalle-ejercicio.component.html',
  styleUrls: ['./detalle-ejercicio.component.css']
})
export class DetalleEjercicioComponent {
[x: string]: any;
  @Input() rutina?: Rutina;
  @Input() ejercicio?: Ejercicio;

  @Output() rutinaEditada = new EventEmitter<Rutina>();
  @Output() rutinaEliminada = new EventEmitter<number>();

  @Output() ejercicioEditado = new EventEmitter<Ejercicio>();
  @Output() ejercicioEliminado = new EventEmitter<number>();

  constructor(
    private rutinasService: RutinasService, 
    private modalService: NgbModal,
    public sanitizer: DomSanitizer  // Añade esta línea
  ) { }


  editarRutina(): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.rutina = {...this.rutina};
    ref.result.then((rutina: Rutina) => {
      this.rutinaEditada.emit(rutina);
    }, (reason) => {});
  }

  eliminarRutina(): void {
    this.rutinaEliminada.emit(this.rutina?.id);
  }

  editarEjercicio(): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.ejercicio = {...this.ejercicio};
    ref.result.then((ejercicio: Ejercicio) => {
      this.ejercicioEditado.emit(ejercicio);
    }, (reason) => {});
  }

  eliminarEjercicio(): void {
    this.ejercicioEliminado.emit(this.ejercicio?.id);
  }

  getEmbedUrl(url: string): SafeResourceUrl {
    let videoId = url.split('v=')[1];
    let embedUrl = 'https://www.youtube.com/embed/' + videoId;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  }
}
