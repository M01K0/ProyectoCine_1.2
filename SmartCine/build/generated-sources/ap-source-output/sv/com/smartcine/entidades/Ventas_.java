package sv.com.smartcine.entidades;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Reservaciones;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Ventas.class)
public class Ventas_ { 

    public static volatile SingularAttribute<Ventas, BigDecimal> monto;
    public static volatile ListAttribute<Ventas, Reservaciones> reservacionesList;
    public static volatile SingularAttribute<Ventas, Long> id;

}