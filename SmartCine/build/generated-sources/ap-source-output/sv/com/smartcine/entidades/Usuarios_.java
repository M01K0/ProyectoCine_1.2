package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Reservaciones;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-05T22:17:42")
@StaticMetamodel(Usuarios.class)
public class Usuarios_ { 

    public static volatile SingularAttribute<Usuarios, String> clave;
    public static volatile ListAttribute<Usuarios, Reservaciones> reservacionesList;
    public static volatile SingularAttribute<Usuarios, String> usuario;
    public static volatile SingularAttribute<Usuarios, Long> id;
    public static volatile SingularAttribute<Usuarios, String> rol;

}