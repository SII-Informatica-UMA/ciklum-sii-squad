import { Component, Input, Output, EventEmitter } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioEjercicioComponent} from '../formulario-ejercicio/formulario-ejercicio.component'
import { Ejercicio } from '../entities/ejercicio';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-detalle-ejercicio',
  templateUrl: './detalle-ejercicio.component.html',
  styleUrls: ['./detalle-ejercicio.component.css']
})
export class DetalleEjercicioComponent {
[x: string]: any;
  @Input() ejercicio?: Ejercicio;
  @Output() ejercicioEditado = new EventEmitter<Ejercicio>();
  @Output() ejercicioEliminado = new EventEmitter<number>();

  constructor(
    private modalService: NgbModal,
    public sanitizer: DomSanitizer  // Añade esta línea
  ) { }

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
