import { Component, Input, Output, EventEmitter } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioRutinaComponent} from '../formulario-rutina/formulario-rutina.component'
import { RutinasService } from '../services/rutina.service';
import { Rutina } from '../entities/rutina';

@Component({
  selector: 'app-detalle-rutina',
  templateUrl: './detalle-rutina.component.html',
  styleUrls: ['./detalle-rutina.component.css']
})
export class DetalleRutinaComponent {
  @Input() rutina?: Rutina;

  @Output() rutinaEditada = new EventEmitter<Rutina>();
  @Output() rutinaEliminada = new EventEmitter<number>();

  constructor(private rutinasService: RutinasService, private modalService: NgbModal) { }

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
}
