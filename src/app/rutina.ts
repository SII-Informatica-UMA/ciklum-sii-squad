import { Ejercicio } from './ejercicio'; 

export interface Rutina {
    id: number;
    nombre: string;
    ejercicios: Ejercicio [];
    descripcion: string;
  }