package com.cibertec.assessment.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.assessment.beans.PolygonBean;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.repo.SquareRepo;
import com.cibertec.assessment.service.PolygonService;
import com.cibertec.assessment.service.SquareService;

@Service
public class SquareServiceImpl implements SquareService{

	@Autowired 
	SquareRepo squareRepo;
	
	@Autowired
	PolygonService polygonService;
	
	//Al momento de crear se debe validar si 
	//alguno de parte del cuadrado se encuentra dentro de algun
	//poligono y de ser asi se debe capturar el id de los poligonos y 
	//guardar como un string pero con formato de array
	//Ejemplo polygons = "["1","2"]"
	//Se guardan los ids correspondites
	//usar los metodos ya existentes para listar polygonos
	@Override
	public Square create(Square s) {
		
		List<PolygonBean> polygonBeans = polygonService.list();
		
		Square square = s;
		
		Integer[] intArrayX = new Integer[square.getNpoints()];
		Integer[] intArrayY = new Integer[square.getNpoints()];
		
		String xcuadrado = square.getXPoints();
		String ycuadrado = square.getYPoints();
		
		boolean dentrox = false;
		boolean dentroy = false;
		List<Integer> polygonsList = new ArrayList<>();
		
		convertStringInIntegerArray(xcuadrado, ycuadrado, intArrayX, intArrayY);
		
		int maximoXC = Arrays.stream(intArrayX)
                .map(Optional::ofNullable)
                .flatMap(Optional::stream)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
		
		int minimoXC = Arrays.stream(intArrayX)
                .map(Optional::ofNullable)
                .flatMap(Optional::stream)
                .min(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
		
		int maximoYC = Arrays.stream(intArrayY)
                .map(Optional::ofNullable)
                .flatMap(Optional::stream)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
		
		int minimoYC = Arrays.stream(intArrayY)
                .map(Optional::ofNullable)
                .flatMap(Optional::stream)
                .min(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
		
		for(int i = 0 ; i < polygonBeans.size(); i++) {
			
			PolygonBean poly = polygonBeans.get(i);
			Integer[] xlist = poly.getXPoints();
			Integer[] ylist = poly.getYPoints();
			
			
			
			int maximoXP = Arrays.stream(xlist)
	                .map(Optional::ofNullable)
	                .flatMap(Optional::stream)
	                .max(Integer::compareTo)
	                .orElse(Integer.MIN_VALUE);
			
			int minimoXP = Arrays.stream(xlist)
	                .map(Optional::ofNullable)
	                .flatMap(Optional::stream)
	                .min(Integer::compareTo)
	                .orElse(Integer.MIN_VALUE);
			
			int maximoYP = Arrays.stream(ylist)
	                .map(Optional::ofNullable)
	                .flatMap(Optional::stream)
	                .max(Integer::compareTo)
	                .orElse(Integer.MIN_VALUE);
			
			int minimoYP = Arrays.stream(ylist)
	                .map(Optional::ofNullable)
	                .flatMap(Optional::stream)
	                .min(Integer::compareTo)
	                .orElse(Integer.MIN_VALUE);

			if(minimoXC <= maximoXP && maximoXP<=maximoXC) {
				dentrox = true;
			}else if(minimoXC <= minimoXP && minimoXP<=maximoXC) {
				dentrox = true;
			}else {
				dentrox = false;
			}
			
			if(minimoYC <= maximoYP && maximoYP <= maximoYC) {
				dentroy = true;
			}else if(minimoYC <= minimoYP && minimoYP <= maximoYC) {
				dentroy = true;
			}else {
				dentroy = false;
			}
			
			if(dentrox == true && dentroy == true) {
				polygonsList.add(poly.getId());
			}
			
			s.setPolygons(polygonsList.toString());
		}
		
		return squareRepo.save(s);
	}

	@Override
	public List<Square> list() {
		return null;
	}
	
	private void convertStringInIntegerArray(String xPoints, String yPoints, Integer[] intArrayX, Integer[] intArrayY) {

		String cleanedXPoints = xPoints.substring(1, xPoints.length() - 1);
		String cleanedYPoints = yPoints.substring(1, yPoints.length() - 1);

		// Split the string by commas
		String[] partsX = cleanedXPoints.split(", ");
		String[] partsY = cleanedYPoints.split(", ");

		for (int i = 0; i < partsX.length; i++) {
			intArrayX[i] = Integer.parseInt(partsX[i]);
		}
		
		for (int i = 0; i < partsY.length; i++) {
			intArrayY[i] = Integer.parseInt(partsY[i]);
		}
	}

}
