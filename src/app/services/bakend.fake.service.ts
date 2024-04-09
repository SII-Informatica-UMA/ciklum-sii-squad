import { Injectable } from "@angular/core";
import { Rutina } from "../entities/rutina";
import { Ejercicio } from "../entities/ejercicio";
import { Observable, of } from "rxjs";


@Injectable({
    providedIn: 'root'
  })
  export class BackendService {

    private rutinas: Rutina [] = [
        {
            id: 1,
            nombre: 'Pecho',
            ejercicios: [
            {
              series: 1,
              repeticiones: 1,
              duracionMinutos: 5,
            ejercicio: {
                id: 1,
                nombre: 'Ejercicio 1',
                descripcion: 'Press de banca con barra',
                observaciones: '',
                duracion: '',
                tipo: 'Pecho',
                musculosTrabajados: 'Pecho',
                material: 'Barra, Banca',
                dificultad: 'Media',
                video: 'https://www.example.com/video',
                foto: 'https://www.example.com/foto'
            }
          },
          {
            series: 1,
            repeticiones: 1,
            duracionMinutos: 5,
          ejercicio:{
                id: 2,
                nombre: 'Ejercicio 2',
                descripcion: 'Press inclinado con mancuernas',
                observaciones: '',
                duracion: '',
                tipo: 'Pecho',
                musculosTrabajados: 'Pecho',
                material: 'Mancuernas',
                dificultad: 'Media',
                video: 'https://www.example.com/video',
                foto: 'https://www.example.com/foto'
            }
          }
            ],
            descripcion: 'Rutina 1',
            observaciones: '',
        },
        {
            id: 2,
            nombre: 'Espalda',
            ejercicios: [
              {
                series: 1,
                repeticiones: 1,
                duracionMinutos: 5,
              ejercicio:{ 
                id: 3,
                nombre: 'Ejercicio 1',
                descripcion: 'Jalon al pecho',
                observaciones: '',
                tipo: 'Espalda',
                duracion: '',
                musculosTrabajados: 'Espalda',
                material: 'Jalon',
                dificultad: 'Media',
                video: 'https://www.example.com/video',
                foto: 'https://www.example.com/foto'
            }
          },
          {
            series: 1,
            repeticiones: 1,
            duracionMinutos: 5,
          ejercicio:{
                id: 4,
                nombre: 'Ejercicio 2',
                descripcion: 'Remo con barra',
                observaciones: '',
                tipo: 'Espalda',
                duracion: '',
                musculosTrabajados: 'Espalda',
                material: 'Remo',
                dificultad: 'Media',
                video: 'https://www.example.com/video',
                foto: 'https://www.example.com/foto'
            }
          }
            ],
            descripcion: 'Rutina 2',
            observaciones: '',
        }
    ];
    
    private ejercicios: Ejercicio [] = [
    {
      id: 1,
      nombre: 'Ejercicio 1',
      descripcion: 'Press de banca con barra',
      observaciones: '',
      tipo: 'Pecho',
      musculosTrabajados: 'Pecho',
      material: 'Barra, Banca',
      dificultad: 'Media',
      video: 'https://www.youtube.com/watch?v=GeLq8cMODLc',
      foto: 'https://www.example.com/foto',
      duracion: ''
    },
    {
      id: 2,
      nombre: 'Ejercicio 2',
      descripcion: 'Press inclinado con mancuernas',
      observaciones: '',
      tipo: 'Pecho',
      musculosTrabajados: 'Pecho',
      material: 'Mancuernas',
      dificultad: 'Media',
      video: 'https://www.youtube.com/watch?v=MkMf308jXww',
      foto: 'https://www.example.com/foto',
      duracion: ''
    },
    {
      id: 3,
      nombre: 'Ejercicio 3',
      descripcion: 'Jalon al pecho',
      observaciones: '',
      tipo: 'Pecho',
      musculosTrabajados: 'Pecho',
      material: 'Jalón',
      dificultad: 'Media',
      video: 'https://www.youtube.com/watch?v=72q0tKij5uU',
      foto: 'https://www.example.com/foto',
      duracion: ''
    }];

    getRutinas(): Observable<Rutina[]> {
        return of(this.rutinas);
    }
    
    postRutina(rutina: Rutina) : Observable<Rutina>{
        rutina.id = Math.max(...this.rutinas.map(c => c.id)) + 1;
        this.rutinas.push(rutina);
        return of(rutina);
    }
    
    putRutina(rutina: Rutina): Observable<Rutina> {
      let indice = this.rutinas.findIndex(c => c.id == rutina.id);
      this.rutinas[indice] = rutina;
      return of(rutina);
    }
    
    deleteRutina(id: number): Observable<number | void> {
      let indice = this.rutinas.findIndex(c => c.id == id);
      this.rutinas.splice(indice, 1);
      return of(id);
    }
    
    getEjercicios(): Observable<Ejercicio[]> {
      return of(this.ejercicios);
    }
    
    postEjercicio(ejercicio: Ejercicio): Observable<Ejercicio> {
      this.ejercicios.push(ejercicio);
      return of(ejercicio);
    }
    
    putEjercicio(ejercicio: Ejercicio): Observable<Ejercicio> {
        let indice = this.ejercicios.findIndex(c => c.id == ejercicio.id);
        this.ejercicios[indice] = ejercicio;
        return of(ejercicio);
    }
    
    deleteEjercicio(id: number): Observable<number | void> {
        let indice = this.ejercicios.findIndex(c => c.id == id);
        this.ejercicios.splice(indice, 1);
        return of(id);
    }

  }