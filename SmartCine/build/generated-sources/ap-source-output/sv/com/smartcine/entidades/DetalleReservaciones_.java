package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Asientos;
import sv.com.smartcine.entidades.Reservaciones;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-05T22:17:42")
@StaticMetamodel(DetalleReservaciones.class)
public class DetalleReservaciones_ { 

    public static volatile SingularAttribute<DetalleReservaciones, Asientos> idAsiento;
    public static volatile SingularAttribute<DetalleReservaciones, Long> id;
    public static volatile SingularAttribute<DetalleReservaciones, Reservaciones> idReservacion;

}