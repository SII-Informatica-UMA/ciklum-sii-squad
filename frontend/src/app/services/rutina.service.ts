import { Injectable } from '@angular/core';
import {Rutina} from '../entities/rutina';
import { Ejercicio } from '../entities/ejercicio';
import { Observable, map, of } from "rxjs";
import { BackendService } from './backend.service';

@Injectable({
  providedIn: 'root'
})

export class RutinasService {
  constructor(private backend: BackendService) { }

  getRutinas(): Observable<Rutina[]> {
    return this.backend.getRutinas(0);
  }

  addRutina(rutina: Rutina) : Observable<Rutina>{ 
    return this.backend.postRutina(rutina,0);
  }

  editarRutina(rutina: Rutina) : Observable<Rutina>{
    return this.backend.putRutina(rutina);
  }

  eliminarRutina(id: number) : Observable<void | number>{
    return this.backend.deleteRutina(id);
  }
}
