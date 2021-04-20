package udp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingTester {
	private final Logger logger = Logger.getLogger(LoggingTester.class
			.getName());
	private FileHandler fh = null;

	public LoggingTester(String ruta) {
		//just to make our log file nicer :)
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler(ruta
					+ format.format(Calendar.getInstance().getTime()) + ".log");
		} catch (Exception e) {
			e.printStackTrace();
		}

		SimpleFormatter formatter = new SimpleFormatter();  
		fh.setFormatter(formatter); 
		logger.addHandler(fh);

		//logger.info();

	}

	public void doLogging(String ruta, boolean entrega, float tiempoTomado, int paquetes) {
		Path p;
		p = Paths.get(ruta);
		long tamano;
		try {
			tamano = Files.size(p);
			p = p.getFileName();
			logger.info("Nombre archivo: "+p+" - Tamaño: " + tamano + " bytes");
			if(entrega ==true){
				logger.info("La entrega del archivo fue exitosa.");
			}
			else{
				logger.info("La entrega del archivo falló");
			}
			logger.info("El tiempo que tomó realizar la transferencia fue de: " + tiempoTomado + " segundos.");
			logger.info("Se enviaron "+paquetes+" paquetes.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}  