package sv.com.smartcine.entidades;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Peliculas;
import sv.com.smartcine.entidades.Reservaciones;
import sv.com.smartcine.entidades.Salas;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Funciones.class)
public class Funciones_ { 

    public static volatile SingularAttribute<Funciones, BigDecimal> precio;
    public static volatile ListAttribute<Funciones, Reservaciones> reservacionesList;
    public static volatile SingularAttribute<Funciones, String> formato;
    public static volatile SingularAttribute<Funciones, Date> fechaHoraInicio;
    public static volatile SingularAttribute<Funciones, Long> id;
    public static volatile SingularAttribute<Funciones, Peliculas> idPelicula;
    public static volatile SingularAttribute<Funciones, Salas> idSala;
    public static volatile SingularAttribute<Funciones, Date> fechaHoraFin;

}