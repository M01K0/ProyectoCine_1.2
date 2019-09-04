package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Peliculas;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Clasificaciones.class)
public class Clasificaciones_ { 

    public static volatile SingularAttribute<Clasificaciones, String> descripcion;
    public static volatile SingularAttribute<Clasificaciones, Long> id;
    public static volatile SingularAttribute<Clasificaciones, Integer> edadMinima;
    public static volatile ListAttribute<Clasificaciones, Peliculas> peliculasList;
    public static volatile SingularAttribute<Clasificaciones, String> nombre;

}