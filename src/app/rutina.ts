import { Ejercicio_rutina } from './ejercicio_rutina'; 

export interface Rutina {
    id: number;
    nombre: string;
    ejercicios: Ejercicio_rutina [];
    descripcion: string;
    observaciones: string;
  }