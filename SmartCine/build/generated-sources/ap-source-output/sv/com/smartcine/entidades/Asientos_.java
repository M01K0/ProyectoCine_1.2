package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.DetalleReservaciones;
import sv.com.smartcine.entidades.Salas;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-05T22:17:42")
@StaticMetamodel(Asientos.class)
public class Asientos_ { 

    public static volatile SingularAttribute<Asientos, String> codigo;
    public static volatile SingularAttribute<Asientos, String> estado;
    public static volatile ListAttribute<Asientos, DetalleReservaciones> detalleReservacionesList;
    public static volatile SingularAttribute<Asientos, Long> id;
    public static volatile SingularAttribute<Asientos, Salas> idSala;

}