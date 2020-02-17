package com.proasecal.software.web.service.administrar;

import com.proasecal.software.web.entity.administrar.Clientes;
import com.proasecal.software.web.entity.inscripcion.PeriodosVigencia;
import com.proasecal.software.web.entity.seguridad.AuditoriaWeb;
import com.proasecal.software.web.entity.seguridad.EnumAccionAuditoria;
import com.proasecal.software.web.repository.administrar.ClienteRepository;
import com.proasecal.software.web.repository.inscripcion.PeriodosVigenciaRepository;
import com.proasecal.software.web.service.seguridad.AuditoriaWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    LaboratorioService laboratorioService;

    @Autowired
    AuditoriaWebService auditoriaWebService;
    @Autowired
    private PeriodosVigenciaRepository periodosVigenciaRepository;

    private String tableName;

    @PostConstruct
    private void init(){
        Class<?> c = Clientes.class;
        Table table = c.getAnnotation(Table.class);
        this.tableName = table.name();
    }

    public Clientes obtenerClienteByID(long idCliente) {
        return clienteRepository.findById(idCliente).get();
    }

    public List<Clientes> getAllClientes() {
        return (List<Clientes>) clienteRepository.findAll();
    }

    public Optional<Clientes> obtenerClientexNombre(Long id) {
        return clienteRepository.findById(id);
    }

    public void guardarActualizarCliente(Clientes clientes) throws Exception {
        clientes.setNumeroIdentificacionCliente(clientes.getNumeroIdentificacionCliente().trim());
        AuditoriaWeb auditoriaWeb =new AuditoriaWeb(tableName,clientes.getClienteId(),clientes.getClienteId()!=0?EnumAccionAuditoria.ACTUALIZAR.getNombreAccion():EnumAccionAuditoria.CREAR.getNombreAccion());
        clienteRepository.save(clientes);
       //Auditoria Save
        try{
            auditoriaWeb.setIdElementoTabla(clientes.getClienteId());
            auditoriaWebService.save(auditoriaWeb);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Clientes obtenerPorDocumento(String docCliente) {
        Clientes clientes = clienteRepository.findClienteByNumeroIdentificacionClienteEquals(docCliente);
        return clientes;
    }

    public List<Clientes> filtrarClientesLike(String nombreCliente) {
        return clienteRepository.findByNumeroIdentificacionClienteContainingOrRazonSocialContainingIgnoreCase(nombreCliente, nombreCliente);
    }


    public boolean existClient(long idCliente, long idLaboratorio) {
        if (!this.laboratorioService.validateEdit(idLaboratorio)) {
            return false;
        }
        Optional<Clientes> clientesList = clienteRepository.findById(idCliente);

        if (clientesList.isPresent()) {
            return false;
        } else {
            return true;
        }
    }


    public Page<Clientes> ListPaginated(String paisFront, String idTipoPais, String razonSocial, String numeroid, String nombreComercial, String estado, Pageable pageable) {

        Page<Clientes> clientesPage= clienteRepository.filtro( paisFront, idTipoPais, numeroid, razonSocial, nombreComercial, estado, pageable);

        return clientesPage;
    }

    public Boolean borrarXId(long id) {
        try {
            Optional<PeriodosVigencia> periodosVigencia = this.periodosVigenciaRepository.buscarxcliente(String.valueOf(id));

            if (!periodosVigencia.isPresent()) {
                Clientes c = new Clientes();
                c.setClienteId(id);
                clienteRepository.delete(c);
                auditoriaWebService.save(new AuditoriaWeb(tableName,id,EnumAccionAuditoria.ELIMINAR.getNombreAccion()));
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
}

