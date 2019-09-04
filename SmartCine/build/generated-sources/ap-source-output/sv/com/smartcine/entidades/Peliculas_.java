package sv.com.smartcine.entidades;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import sv.com.smartcine.entidades.Clasificaciones;
import sv.com.smartcine.entidades.Funciones;
import sv.com.smartcine.entidades.Genero;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-03T15:53:32")
@StaticMetamodel(Peliculas.class)
public class Peliculas_ { 

    public static volatile ListAttribute<Peliculas, Funciones> funcionesList;
    public static volatile SingularAttribute<Peliculas, Genero> idGenero;
    public static volatile SingularAttribute<Peliculas, String> titulo;
    public static volatile SingularAttribute<Peliculas, String> duracion;
    public static volatile SingularAttribute<Peliculas, Clasificaciones> idClasificacion;
    public static volatile SingularAttribute<Peliculas, Long> id;
    public static volatile SingularAttribute<Peliculas, String> sinopsis;

}