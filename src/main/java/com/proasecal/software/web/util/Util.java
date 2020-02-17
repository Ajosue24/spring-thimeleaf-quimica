package com.proasecal.software.web.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.proasecal.software.controlexterno.entity.DAO.ConsensoInicial;
import com.proasecal.software.web.entity.administrar.Mensurandos;
import com.proasecal.software.web.entity.seguridad.Modulos;
import com.proasecal.software.web.entity.seguridad.Roles;
import com.proasecal.software.web.service.parametricas.VariablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.apache.commons.math3.distribution.TDistribution;

import com.proasecal.software.web.entity.seguridad.CustomUserDetail;
import com.proasecal.software.web.entity.seguridad.Permisos;
@PropertySource("classpath:static/properties/msg.properties")
public class Util {

	private static final Logger LOG = LoggerFactory.getLogger(Util.class);
	
    @Autowired
    Environment env;


    @Autowired
	public String getVersion() {
		return env.getProperty("app.version");
	}
	
	/**
	 * Valida si usuario esta authenticado
	 */
	public static CustomUserDetail userAuth() {
		try {
			Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
			if (auth.isPresent()) {
				return (CustomUserDetail) auth.get().getPrincipal();
			} else {
				return null;
			}
		}catch (ClassCastException e){
			//Session perdida o Inexistente
			return null;
		}
	}

	/**
	 * Permisos al usuario por la url
	 */
	public static Optional<List<Permisos>> userPermissions() {
		try {
			Optional<CustomUserDetail> usuario = Optional.ofNullable(Util.userAuth());
			if (usuario.isPresent()) {
				List<Roles> rolesList = usuario.get().getRolesList().stream().filter(x->x.getEstado()).collect(Collectors.toList());
				return Optional.ofNullable(rolesList.stream().map(x -> x.getPermisosList()).flatMap(x -> x.stream()).collect(Collectors.toList()));
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
				LOG.error("Error al obtener las urls a las cuales usuario");
			return Optional.empty();
		}
	}

	/**
	 * Obtiene a los modulos que estan asociado al rol
	 */
	public static Optional<List<Modulos>> userModules() {
		Optional<CustomUserDetail> usuario = Optional.ofNullable(Util.userAuth());
		if (usuario.isPresent()) {
			return Optional.ofNullable(usuario.get().getRolesList().stream().map(x -> x.getModulosList()).flatMap(x -> x.stream()).collect(Collectors.toList()));
		} else {
			return Optional.empty();
		}
	}

	public static boolean ifJsorCssorJpg(String url) {
		//TODO: crear enum con propiedades
		if (	url.endsWith("js") ||
				url.endsWith("css") ||
				url.endsWith("jpg") ||
				url.endsWith("png") ||
				url.endsWith("woff2") ||
				url.endsWith("woff")||
				url.endsWith("gif")||
				url.endsWith("map")||
				url.endsWith("svg")||
				url.endsWith("pdf")||
				url.startsWith("/rest")||
				url.startsWith("txt")) {
			return true;
		}
		return false;
	}

	public static String removeIdFromUrl(String url) {
		String urlNew = "";
		String[] arrayUrl = url.split("/");
		for (String segment : arrayUrl) {
			if (!isInteger(segment)) {
				urlNew += segment + "/";
			}
		}
		return urlNew;
	}

	private static Boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}
		return true;
	}


	/**
	 * Valida si url es igual con o sin slash ("/")
	 */
	public static Boolean urlCompareUrlDB(String urlDb,String url){
		Boolean isvalid=urlDb.replaceAll("/","").equalsIgnoreCase(url.replaceAll("/",""));
		return isvalid;
	}

	/**
	 * Para el SP de Simulacion obtener grubbs
	 */
	public static Double grubbs(int n_entero, double n_significancia) {
		TDistribution t = new TDistribution((n_entero - 2));
		double calculo = Math.pow(t.inverseCumulativeProbability(1 - n_significancia / (2 * n_entero)), 2);
		DecimalFormat df = new DecimalFormat("#.00");
		double grubbs = (((n_entero - 1) / Math.sqrt(n_entero)) * Math.sqrt(calculo / ((n_entero - 2) + calculo)));
		BigDecimal numberBigDecimal = new BigDecimal(grubbs, MathContext.DECIMAL64);
		numberBigDecimal = numberBigDecimal.setScale(2, RoundingMode.DOWN);

		return (numberBigDecimal.doubleValue());
	}

	/**
	 * Funcion que realiza split de String y retorna un objeto ConsesoInicial
	 */
	public static ConsensoInicial parseStringToConsensoInicial(String resultado) {
		String[] parts = resultado.split(";");
		ConsensoInicial consensoInicial = new ConsensoInicial();
		try {
			consensoInicial.setMedia(Double.parseDouble(parts[0].equalsIgnoreCase("")?"0":parts[0]));
			consensoInicial.setDesviacionEstandar(Double.parseDouble(parts[1].equalsIgnoreCase("")?"0":parts[1]));
			consensoInicial.setValorMinimo(Double.parseDouble(parts[2].equalsIgnoreCase("")?"0":parts[2]));
			consensoInicial.setValorMaximo(Double.parseDouble(parts[3].equalsIgnoreCase("")?"0":parts[3]));
			consensoInicial.setTotalElementos(Integer.parseInt(parts[4].equalsIgnoreCase("")?"0":parts[4]));
			consensoInicial.setTotalAberrantes(Integer.parseInt(parts[5].equalsIgnoreCase("")?"0":parts[5]));
			return consensoInicial;
		} catch (Exception e) {
			return new ConsensoInicial();
		}
	}

	public static ConsensoInicial parseStringToConsensoInicialGrubbs(String resultado) {
		String[] parts = resultado.split(";");
		ConsensoInicial consensoInicial = new ConsensoInicial();
		try {
			consensoInicial.setMedia(Double.parseDouble(parts[0].equalsIgnoreCase("")?"0":parts[0]));
			consensoInicial.setDesviacionEstandar(Double.parseDouble(parts[1].equalsIgnoreCase("")?"0":parts[1]));
			consensoInicial.setTotalElementos(Integer.parseInt(parts[2].equalsIgnoreCase("")?"0":parts[2]));
			consensoInicial.setTotalAberrantes(Integer.parseInt(parts[3].equalsIgnoreCase("")?"0":parts[3]));
			consensoInicial.setEsAberranteMin(parts[4].equalsIgnoreCase("f") ? false : true);
			consensoInicial.setAberranteMin(Double.parseDouble(parts[5].equalsIgnoreCase("")?"0":parts[5]));
			consensoInicial.setEsAberranteMax(parts[6].equalsIgnoreCase("f") ? false : true);
			consensoInicial.setAberranteMax(Double.parseDouble(parts[7].equalsIgnoreCase("")?"0":parts[7]));
			return consensoInicial;
		} catch (Exception e) {
			return new ConsensoInicial();
		}
	}
}