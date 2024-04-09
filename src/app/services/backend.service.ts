import { Injectable } from "@angular/core";
import { Observable, map, of } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Rutina } from "../entities/rutina";
import { Ejercicio } from "../entities/ejercicio";

@Injectable({
    providedIn: 'root'
  })
export class BackendService {

    readonly BACKEND_URI = 'http://localhost:8080';

    constructor(private httpClient: HttpClient) {}

    getRutina(id: number): Observable<Rutina> {
        return this.httpClient.get<Rutina>(this.BACKEND_URI + '/rutina/' + id);
    }

    putRutina(rutina: Rutina): Observable<Rutina> {
        return this.httpClient.put<Rutina>(this.BACKEND_URI + '/rutina/' + rutina.id, rutina);
    }

    deleteRutina(id: number): Observable<void> {
        return this.httpClient.delete<void>(this.BACKEND_URI + '/rutina/' + id);
    }

    getEjercicio(id: number): Observable<Ejercicio> {
        return this.httpClient.get<Ejercicio>(this.BACKEND_URI + '/ejercicio/' + id);
    }

    putEjercicio(ejercicio: Ejercicio): Observable<Ejercicio> {
        return this.httpClient.put<Ejercicio>(this.BACKEND_URI + '/ejercicio/' + ejercicio.id, ejercicio);
    }

    deleteEjercicio(id: number): Observable<void> {
        return this.httpClient.delete<void>(this.BACKEND_URI + '/ejercicio/' + id);
    }

    getRutinas(): Observable<Rutina[]> {
        return this.httpClient.get<Rutina[]>(this.BACKEND_URI + '/rutina');
    }

    postRutina(rutina: Rutina): Observable<Rutina> {
        return this.httpClient.post<Rutina>(this.BACKEND_URI + '/rutina', rutina);
    }

    getEjercicios(): Observable<Ejercicio[]> {
        return this.httpClient.get<Ejercicio[]>(this.BACKEND_URI + '/ejercicio');
    }

    postEjercicio(ejercicio: Ejercicio): Observable<Ejercicio> {
        return this.httpClient.post<Ejercicio>(this.BACKEND_URI + '/ejercicio', ejercicio);
    }
}