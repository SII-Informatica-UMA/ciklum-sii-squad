import { Injectable } from '@angular/core';
import {Rutina} from './rutina';
import { Ejercicio } from './ejercicio';
import { BackendService } from "./backend.service";
import { Observable, map, of } from "rxjs";
import { BackendFakeService } from './bakend.fake.service';

@Injectable({
  providedIn: 'root'
})
export class RutinasService {
    constructor(private backend: BackendFakeService) { }

  getRutinas(): Observable<Rutina[]> {
    return this.backend.getRutinas();
  }

  addRutina(rutina: Rutina) : Observable<Rutina>{ 
    return this.backend.postRutina(rutina);
  }

  editarRutina(rutina: Rutina) : Observable<Rutina>{
    return this.backend.putRutina(rutina);
  }

  eliminarRutina(id: number) : Observable<void | number>{
    return this.backend.deleteRutina(id);
  }

  getEjercicios(): Observable<Ejercicio []> {
    return this.backend.getEjercicios();
  }

  addEjercicio(ejercicio: Ejercicio) : Observable<Ejercicio>{
    return this.backend.postEjercicio(ejercicio);
  }

  editarEjercicio(ejercicio: Ejercicio) : Observable<Ejercicio> {
    return this.backend.putEjercicio(ejercicio);
  }

  eliminarEjercicio(id: number) : Observable<void | number> {
    return this.backend.deleteEjercicio(id);
  }
}
