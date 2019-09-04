package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Asientos;
import sv.com.smartcine.entidades.Funciones;
import sv.com.smartcine.entidades.Sucursales;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Salas.class)
public class Salas_ { 

    public static volatile SingularAttribute<Salas, Sucursales> idSucursal;
    public static volatile SingularAttribute<Salas, Integer> numero;
    public static volatile ListAttribute<Salas, Funciones> funcionesList;
    public static volatile ListAttribute<Salas, Asientos> asientosList;
    public static volatile SingularAttribute<Salas, Long> id;

}