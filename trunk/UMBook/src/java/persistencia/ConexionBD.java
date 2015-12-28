package persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionBD {

    private static ConexionBD conexion;
    private EntityManager em;

    private ConexionBD() {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("UMBookPU");
            em = emf.createEntityManager();
    }

    public static ConexionBD getInstancia(){
        if (conexion == null)
            conexion = new ConexionBD();
        return conexion;
    }

    public EntityManager getEm() {
        return em;
    }
}
