create sequence asignacion_entrenamiento_seq start with 1 increment by 50;
create sequence centro_seq start with 1 increment by 50;
create sequence cliente_seq start with 1 increment by 50;
create sequence dieta_seq start with 1 increment by 50;
create sequence ejercicio_seq start with 1 increment by 50;
create sequence entrenador_seq start with 1 increment by 50;
create sequence evento_seq start with 1 increment by 50;
create sequence fragmento_rutina_seq start with 1 increment by 50;
create sequence gerente_seq start with 1 increment by 50;
create sequence mensaje_centro_seq start with 1 increment by 50;
create sequence mensaje_cliente_seq start with 1 increment by 50;
create sequence mensaje_entrenador_seq start with 1 increment by 50;
create sequence plan_seq start with 1 increment by 50;
create sequence rutina_seq start with 1 increment by 50;
create sequence sesion_seq start with 1 increment by 50;
create sequence usuario_seq start with 1 increment by 50;
create table asignacion_entrenamiento (id bigint not null, id_cliente bigint, id_entrenador bigint, especialidad varchar(255), primary key (id));
create table centro (gerente_id bigint unique, id bigint not null, direccion varchar(255), nombre varchar(255), primary key (id));
create table cliente (fecha_nacimiento date, sexo tinyint check (sexo between 0 and 2), id bigint not null, id_centro bigint, id_usuario bigint, direccion varchar(255), dni varchar(255), telefono varchar(255), primary key (id));
create table dieta (duracion_dias integer, id bigint not null, id_entrenador bigint, descripcion varchar(255), nombre varchar(255), objetivo varchar(255), observaciones varchar(255), recomendaciones varchar(255), primary key (id));
create table dieta_alimentos (dieta_id bigint not null, alimentos varchar(255));
create table dieta_id_clientes (dieta_id bigint not null, id_clientes bigint);
create table ejercicio (id bigint not null, id_entrenador bigint, descripcion varchar(255), dificultad varchar(255), material varchar(255), musculos_trabajados varchar(255), nombre varchar(255), observaciones varchar(255), tipo varchar(255), primary key (id));
create table ejercicio_multimedia (ejercicio_id bigint not null, multimedia varchar(255));
create table entrenador (fecha_alta date, fecha_baja date, fecha_nacimiento date, id bigint not null, id_centro bigint, id_usuario bigint, direccion varchar(255), dni varchar(255), especialidad varchar(255), experiencia varchar(255), observaciones varchar(255), telefono varchar(255), titulacion varchar(255), primary key (id));
create table evento (duracion_minutos bigint not null, id bigint not null, id_cliente bigint, id_entrenador bigint not null, inicio timestamp(6) not null, descripcion varchar(255), lugar varchar(255), nombre varchar(255), observaciones varchar(255), regla_recurrencia varchar(255), tipo varchar(255) not null check (tipo in ('DISPONIBILIDAD','CITA')), primary key (id));
create table fragmento_rutina (duracion_minutos integer, repeticiones integer, series integer, ejercicio_id bigint, id bigint not null, primary key (id));
create table gerente (id bigint not null, id_usuario bigint not null, empresa varchar(255), primary key (id));
create table mensaje_centro (centro_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_centro_copia (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_centro_copia_oculta (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_centro_destinatarios (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente (cliente_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_cliente_copia (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente_copia_oculta (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente_destinatarios (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador (entrenador_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_entrenador_copia (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador_copia_oculta (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador_destinatarios (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table password_reset (token_creation timestamp(6), usuario_id bigint, token varchar(255) not null, primary key (token));
create table plan (fecha_fin date, fecha_inicio date, asignacion_entrenamiento_id bigint, id bigint not null, id_rutina bigint, regla_recurrencia varchar(255), primary key (id));
create table rutina (id bigint not null, id_entrenador bigint, descripcion varchar(255), nombre varchar(255), observaciones varchar(255), primary key (id));
create table rutina_ejercicios (ejercicios_id bigint not null unique, rutina_id bigint not null);
create table sesion (presencial boolean, fin timestamp(6), id bigint not null, id_plan bigint not null, inicio timestamp(6), descripcion varchar(255), trabajo_realizado varchar(255), primary key (id));
create table sesion_datos_salud (sesion_id bigint not null, datos_salud varchar(255));
create table sesion_multimedia (sesion_id bigint not null, multimedia varchar(255));
create table usuario (administrador boolean, id bigint not null, apellido1 varchar(255), apellido2 varchar(255), email varchar(255) not null unique, nombre varchar(255), password varchar(255), primary key (id));
alter table if exists centro add constraint FK5ifia5s8ng6x1kyyckbo93arf foreign key (gerente_id) references gerente;
alter table if exists dieta_alimentos add constraint FKgp9v0i9bk4etmieq7od891n94 foreign key (dieta_id) references dieta;
alter table if exists dieta_id_clientes add constraint FK8lpdga07183lt7ees9akd5mke foreign key (dieta_id) references dieta;
alter table if exists ejercicio_multimedia add constraint FKqfgc5ud05wxurkemm8qnw20n1 foreign key (ejercicio_id) references ejercicio;
alter table if exists fragmento_rutina add constraint FK85d8i9rl0h1b5hpder6lakp2l foreign key (ejercicio_id) references ejercicio;
alter table if exists mensaje_centro add constraint FK5rlmc3rdbx3pxq1j5wrmoj4v0 foreign key (centro_id) references centro;
alter table if exists mensaje_centro_copia add constraint FKkwgeevsv909hbyfdq2fxh28ls foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_centro_copia_oculta add constraint FK2y11sv6avhm68gf2t9j1wqfta foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_centro_destinatarios add constraint FKsqdn9bd9995u67267b4p396fb foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_cliente add constraint FKml5gaaitecmos69msr3bo3ua7 foreign key (cliente_id) references cliente;
alter table if exists mensaje_cliente_copia add constraint FKdn7cmr0lkx6xore5597sww68o foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_cliente_copia_oculta add constraint FKqa7n6qjbnyqnm53st3golw0de foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_cliente_destinatarios add constraint FKaoud9q0gys82htt490bbhj7cd foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_entrenador add constraint FKlvxg8o6et3n9gpqg6vqod0nwk foreign key (entrenador_id) references entrenador;
alter table if exists mensaje_entrenador_copia add constraint FK3rrfuy1lghpu0sw6qjftc30s1 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists mensaje_entrenador_copia_oculta add constraint FK2r3vum0eq940dix6f5qwi9p95 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists mensaje_entrenador_destinatarios add constraint FKouu2knm4drsivulcuocp7con3 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists password_reset add constraint FKgarut7q0yocc0f1vs1mhgidn1 foreign key (usuario_id) references usuario;
alter table if exists plan add constraint FK5ojvp7dwnvy8rgdg4mmmu21tc foreign key (asignacion_entrenamiento_id) references asignacion_entrenamiento;
alter table if exists rutina_ejercicios add constraint FKq6jic6isp5qhj6q6lkus7s5uv foreign key (ejercicios_id) references fragmento_rutina;
alter table if exists rutina_ejercicios add constraint FKmcb9eic73952ml9qd0113uq1n foreign key (rutina_id) references rutina;
alter table if exists sesion_datos_salud add constraint FKsuv6hr652pkc6f9yyygyeuy18 foreign key (sesion_id) references sesion;
alter table if exists sesion_multimedia add constraint FKqw93cgvsw1chd09u7ybcp54y9 foreign key (sesion_id) references sesion;
create sequence asignacion_entrenamiento_seq start with 1 increment by 50;
create sequence centro_seq start with 1 increment by 50;
create sequence cliente_seq start with 1 increment by 50;
create sequence dieta_seq start with 1 increment by 50;
create sequence ejercicio_seq start with 1 increment by 50;
create sequence entrenador_seq start with 1 increment by 50;
create sequence evento_seq start with 1 increment by 50;
create sequence fragmento_rutina_seq start with 1 increment by 50;
create sequence gerente_seq start with 1 increment by 50;
create sequence mensaje_centro_seq start with 1 increment by 50;
create sequence mensaje_cliente_seq start with 1 increment by 50;
create sequence mensaje_entrenador_seq start with 1 increment by 50;
create sequence plan_seq start with 1 increment by 50;
create sequence rutina_seq start with 1 increment by 50;
create sequence sesion_seq start with 1 increment by 50;
create sequence usuario_seq start with 1 increment by 50;
create table asignacion_entrenamiento (id bigint not null, id_cliente bigint, id_entrenador bigint, especialidad varchar(255), primary key (id));
create table centro (gerente_id bigint unique, id bigint not null, direccion varchar(255), nombre varchar(255), primary key (id));
create table cliente (fecha_nacimiento date, sexo tinyint check (sexo between 0 and 2), id bigint not null, id_centro bigint, id_usuario bigint, direccion varchar(255), dni varchar(255), telefono varchar(255), primary key (id));
create table dieta (duracion_dias integer, id bigint not null, id_entrenador bigint, descripcion varchar(255), nombre varchar(255), objetivo varchar(255), observaciones varchar(255), recomendaciones varchar(255), primary key (id));
create table dieta_alimentos (dieta_id bigint not null, alimentos varchar(255));
create table dieta_id_clientes (dieta_id bigint not null, id_clientes bigint);
create table ejercicio (id bigint not null, id_entrenador bigint, descripcion varchar(255), dificultad varchar(255), material varchar(255), musculos_trabajados varchar(255), nombre varchar(255), observaciones varchar(255), tipo varchar(255), primary key (id));
create table ejercicio_multimedia (ejercicio_id bigint not null, multimedia varchar(255));
create table entrenador (fecha_alta date, fecha_baja date, fecha_nacimiento date, id bigint not null, id_centro bigint, id_usuario bigint, direccion varchar(255), dni varchar(255), especialidad varchar(255), experiencia varchar(255), observaciones varchar(255), telefono varchar(255), titulacion varchar(255), primary key (id));
create table evento (duracion_minutos bigint not null, id bigint not null, id_cliente bigint, id_entrenador bigint not null, inicio timestamp(6) not null, descripcion varchar(255), lugar varchar(255), nombre varchar(255), observaciones varchar(255), regla_recurrencia varchar(255), tipo varchar(255) not null check (tipo in ('DISPONIBILIDAD','CITA')), primary key (id));
create table fragmento_rutina (duracion_minutos integer, repeticiones integer, series integer, ejercicio_id bigint, id bigint not null, primary key (id));
create table gerente (id bigint not null, id_usuario bigint not null, empresa varchar(255), primary key (id));
create table mensaje_centro (centro_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_centro_copia (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_centro_copia_oculta (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_centro_destinatarios (id_destinatario bigint, mensaje_centro_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente (cliente_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_cliente_copia (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente_copia_oculta (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_cliente_destinatarios (id_destinatario bigint, mensaje_cliente_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador (entrenador_id bigint, id bigint not null, id_destinatario bigint, asunto varchar(255), contenido varchar(255), tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')), primary key (id));
create table mensaje_entrenador_copia (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador_copia_oculta (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table mensaje_entrenador_destinatarios (id_destinatario bigint, mensaje_entrenador_id bigint not null, tipo varchar(255) check (tipo in ('CENTRO','ENTRENADOR','CLIENTE')));
create table password_reset (token_creation timestamp(6), usuario_id bigint, token varchar(255) not null, primary key (token));
create table plan (fecha_fin date, fecha_inicio date, asignacion_entrenamiento_id bigint, id bigint not null, id_rutina bigint, regla_recurrencia varchar(255), primary key (id));
create table rutina (id bigint not null, id_entrenador bigint, descripcion varchar(255), nombre varchar(255), observaciones varchar(255), primary key (id));
create table rutina_ejercicios (ejercicios_id bigint not null unique, rutina_id bigint not null);
create table sesion (presencial boolean, fin timestamp(6), id bigint not null, id_plan bigint not null, inicio timestamp(6), descripcion varchar(255), trabajo_realizado varchar(255), primary key (id));
create table sesion_datos_salud (sesion_id bigint not null, datos_salud varchar(255));
create table sesion_multimedia (sesion_id bigint not null, multimedia varchar(255));
create table usuario (administrador boolean, id bigint not null, apellido1 varchar(255), apellido2 varchar(255), email varchar(255) not null unique, nombre varchar(255), password varchar(255), primary key (id));
alter table if exists centro add constraint FK5ifia5s8ng6x1kyyckbo93arf foreign key (gerente_id) references gerente;
alter table if exists dieta_alimentos add constraint FKgp9v0i9bk4etmieq7od891n94 foreign key (dieta_id) references dieta;
alter table if exists dieta_id_clientes add constraint FK8lpdga07183lt7ees9akd5mke foreign key (dieta_id) references dieta;
alter table if exists ejercicio_multimedia add constraint FKqfgc5ud05wxurkemm8qnw20n1 foreign key (ejercicio_id) references ejercicio;
alter table if exists fragmento_rutina add constraint FK85d8i9rl0h1b5hpder6lakp2l foreign key (ejercicio_id) references ejercicio;
alter table if exists mensaje_centro add constraint FK5rlmc3rdbx3pxq1j5wrmoj4v0 foreign key (centro_id) references centro;
alter table if exists mensaje_centro_copia add constraint FKkwgeevsv909hbyfdq2fxh28ls foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_centro_copia_oculta add constraint FK2y11sv6avhm68gf2t9j1wqfta foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_centro_destinatarios add constraint FKsqdn9bd9995u67267b4p396fb foreign key (mensaje_centro_id) references mensaje_centro;
alter table if exists mensaje_cliente add constraint FKml5gaaitecmos69msr3bo3ua7 foreign key (cliente_id) references cliente;
alter table if exists mensaje_cliente_copia add constraint FKdn7cmr0lkx6xore5597sww68o foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_cliente_copia_oculta add constraint FKqa7n6qjbnyqnm53st3golw0de foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_cliente_destinatarios add constraint FKaoud9q0gys82htt490bbhj7cd foreign key (mensaje_cliente_id) references mensaje_cliente;
alter table if exists mensaje_entrenador add constraint FKlvxg8o6et3n9gpqg6vqod0nwk foreign key (entrenador_id) references entrenador;
alter table if exists mensaje_entrenador_copia add constraint FK3rrfuy1lghpu0sw6qjftc30s1 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists mensaje_entrenador_copia_oculta add constraint FK2r3vum0eq940dix6f5qwi9p95 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists mensaje_entrenador_destinatarios add constraint FKouu2knm4drsivulcuocp7con3 foreign key (mensaje_entrenador_id) references mensaje_entrenador;
alter table if exists password_reset add constraint FKgarut7q0yocc0f1vs1mhgidn1 foreign key (usuario_id) references usuario;
alter table if exists plan add constraint FK5ojvp7dwnvy8rgdg4mmmu21tc foreign key (asignacion_entrenamiento_id) references asignacion_entrenamiento;
alter table if exists rutina_ejercicios add constraint FKq6jic6isp5qhj6q6lkus7s5uv foreign key (ejercicios_id) references fragmento_rutina;
alter table if exists rutina_ejercicios add constraint FKmcb9eic73952ml9qd0113uq1n foreign key (rutina_id) references rutina;
alter table if exists sesion_datos_salud add constraint FKsuv6hr652pkc6f9yyygyeuy18 foreign key (sesion_id) references sesion;
alter table if exists sesion_multimedia add constraint FKqw93cgvsw1chd09u7ybcp54y9 foreign key (sesion_id) references sesion;
