package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.DetalleReservaciones;
import sv.com.smartcine.entidades.Funciones;
import sv.com.smartcine.entidades.Usuarios;
import sv.com.smartcine.entidades.Ventas;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Reservaciones.class)
public class Reservaciones_ { 

    public static volatile SingularAttribute<Reservaciones, String> metodoPago;
    public static volatile ListAttribute<Reservaciones, DetalleReservaciones> detalleReservacionesList;
    public static volatile SingularAttribute<Reservaciones, Usuarios> idUsuario;
    public static volatile SingularAttribute<Reservaciones, Funciones> idFuncion;
    public static volatile SingularAttribute<Reservaciones, Long> id;
    public static volatile SingularAttribute<Reservaciones, Integer> cantidad;
    public static volatile SingularAttribute<Reservaciones, Ventas> idVentas;

}