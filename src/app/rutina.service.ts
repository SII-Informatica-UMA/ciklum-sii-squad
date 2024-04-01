import { Injectable } from '@angular/core';
import {Rutina} from './rutina';
import { Ejercicio } from './ejercicio';

@Injectable({
  providedIn: 'root'
})
export class RutinasService {
  

  private rutinas: Rutina [] = [
    {
        id: 1,
        nombre: 'Pecho',
        ejercicios: [
        {
            id: 1,
            nombre: 'Ejercicio 1',
            series: 4,
            repeticiones: '8-10',
            descripcion: 'Press de banca con barra',
            video: 'https://www.example.com/video',
            foto: 'https://www.example.com/foto'
        },
        {
            id: 2,
            nombre: 'Ejercicio 2',
            series: 4,
            repeticiones: '10-12',
            descripcion: 'Press inclinado con mancuernas',
            video: 'https://www.example.com/video',
            foto: 'https://www.example.com/foto'
        }
        ],
        descripcion: 'Rutina 1',
    },
    {
        id: 2,
        nombre: 'Espalda',
        ejercicios: [
        { 
            id: 3,
            nombre: 'Ejercicio 1',
            series: 4,
            repeticiones: '8-10',
            descripcion: 'Jalon al pecho',
            video: 'https://www.example.com/video',
            foto: 'https://www.example.com/foto'
        },
        {
            id: 4,
            nombre: 'Ejercicio 2',
            series: 4,
            repeticiones: '10-12',
            descripcion: 'Remo con barra',
            video: 'https://www.example.com/video',
            foto: 'https://www.example.com/foto'
        }
        ],
        descripcion: 'Rutina 2',
    }
];

private ejercicios: Ejercicio [] = [
{
  id: 1,
  nombre: 'Ejercicio 1',
  series: 4,
  repeticiones: '8-10',
  descripcion: 'Press de banca con barra',
  video: 'https://www.example.com/video',
  foto: 'https://www.example.com/foto'
},
{
  id: 2,
  nombre: 'Ejercicio 2',
  series: 4,
  repeticiones: '10-12',
  descripcion: 'Press inclinado con mancuernas',
  video: 'https://www.example.com/video',
  foto: 'https://www.example.com/foto'
},
{
  id: 3,
  nombre: 'Ejercicio 3',
  series: 4,
  repeticiones: '8-12',
  descripcion: 'Jalon al pecho',
  video: 'https://www.example.com/video',
  foto: 'https://www.example.com/foto'
}];


  constructor() { }



  getRutinas(): Rutina [] {
    return this.rutinas;
  }

  addRutina(rutina: Rutina) {
    rutina.id = Math.max(...this.rutinas.map(c => c.id)) + 1;
    this.rutinas.push(rutina);
  }

  editarRutina(rutina: Rutina) {
    let indice = this.rutinas.findIndex(c => c.id == rutina.id);
    this.rutinas[indice] = rutina;
  }

  eliminarRutina(id: number) {
    let indice = this.rutinas.findIndex(c => c.id == id);
    this.rutinas.splice(indice, 1);
  }

  getEjercicios(): Ejercicio [] {
    return this.ejercicios;
  }

  addEjercicio(ejercicio: Ejercicio) {
    this.ejercicios.push(ejercicio);
  }

  editarEjercicio(ejercicio: Ejercicio) {
    let indice = this.ejercicios.findIndex(c => c.nombre == ejercicio.nombre);
    this.ejercicios[indice] = ejercicio;
  }

  eliminarEjercicio(id: number) {
    let indice = this.ejercicios.findIndex(c => c.id == id);
    this.ejercicios.splice(indice, 1);
  }
}
