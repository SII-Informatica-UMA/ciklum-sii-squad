import { Ejercicio } from './ejercicio'; 

export interface Ejercicio_rutina {
    series: number;
    repeticiones: number;
    duracionMinutos: number;
    ejercicio: Ejercicio;
  }