import { Injectable } from '@angular/core';
import {Rutina} from '../entities/rutina';
import { Ejercicio } from '../entities/ejercicio';
import { Observable, map, of } from "rxjs";
import { BackendService } from './bakend.fake.service';


@Injectable({
    providedIn: 'root'
  })

export class EjercicioService {
  constructor(private backend: BackendService) { }
  
  getEjercicios(): Observable<Ejercicio []> {
    return this.backend.getEjercicios(0);
  }

  addEjercicio(ejercicio: Ejercicio) : Observable<Ejercicio>{
    return this.backend.postEjercicio(ejercicio,0);
  }

  editarEjercicio(ejercicio: Ejercicio) : Observable<Ejercicio> {
    return this.backend.putEjercicio(ejercicio);
  }

  eliminarEjercicio(id: number) : Observable<void | number> {
    return this.backend.deleteEjercicio(id);
  }
}