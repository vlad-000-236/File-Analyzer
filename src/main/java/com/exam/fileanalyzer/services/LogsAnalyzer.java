package com.exam.fileanalyzer.services;

import com.exam.fileanalyzer.models.ParamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class LogsAnalyzer implements LogsAnalyzerInterface {

	ParamModel paramModel;

	@Autowired
	public LogsAnalyzer(ParamModel paramModel){
		this.paramModel = paramModel;
	}

	@Override
	public String zipOpener(File fileName) {
		File directory = new File("src\\main\\resources\\unzipedFiles");
		if(!directory.exists()){
			directory.mkdir();
		}

		try(ZipInputStream zipFile = new ZipInputStream(new FileInputStream(fileName))){

			ZipEntry entry;
			String name;

			while ((entry = zipFile.getNextEntry()) != null){
				name = entry.getName();

				FileOutputStream fileOut = new FileOutputStream("src\\main\\resources\\unzipedFiles\\" + name);

				for (int a = zipFile.read(); a != -1; a = zipFile.read()) {
					fileOut.write(a);
				}
				fileOut.flush();
				zipFile.closeEntry();
				fileOut.close();
				}
			}
		catch (Exception e){
		}
		return "src\\main\\resources\\unzipedFiles\\";
	}

	@Override
	public ArrayList<File> unzipedFilesChecker(String unzipedFilesPath, LocalDate userDate, int numOfDays) {
		File folder = new File(unzipedFilesPath);
		File[] files = folder.listFiles();
		ArrayList <File> filesForPeriod = new ArrayList<>();

		for (File file : files) {
			if (file.isFile()) {
				String fileNameDate = file.getName().substring(5,15);
				LocalDate fileDate = LocalDate.parse(fileNameDate);

				if((userDate.isEqual(fileDate) || userDate.isBefore(fileDate)) && numOfDays > 0){
					filesForPeriod.add(file);
					numOfDays--;
				}
			}
		}
		return filesForPeriod;
	}

	@Override
	public Map<String, Integer> parser(ArrayList <File> filesList, String userString) {
		HashMap <String, Integer> result = new HashMap<>();

		for (int a = 0; a < filesList.size(); a++){

			int count = 0;

			try (BufferedReader reader = new BufferedReader(new FileReader(filesList.get(a)))) {
				String line;
				boolean contains;

				while ((line = reader.readLine()) != null) {
					if ( contains = line.contains(userString)){
						count++;
					}
				}
			} catch (IOException e) {
				System.err.println("Ошибка при чтении файла: " + e.getMessage());
			}

			if (count != 0){
				result.put(filesList.get(a).getName(), count);
			}
		}
		return result;
	}

	/**
	 * Given a zip file, a search query, and a date range,
	 * count the number of occurrences of the search query in each file in the zip file
	 *
	 * @param searchQuery The string to search for in the file.
	 * @param zipFile The zip file to search in.
	 * @param startDate The start date of the search.
	 * @param numberOfDays The number of days to search for.
	 * @return A map of file names and the number of occurrences of the search query in the file.
	 */
	public Map<String, Integer> countEntriesInZipFile(
			String searchQuery, File zipFile, LocalDate startDate, Integer numberOfDays) {

		paramModel.setSearchQuery(searchQuery);
		paramModel.setZipFile(zipFile);
		paramModel.setStartDate(startDate);
		paramModel.setNumberOfDays(numberOfDays);

		String unzipedFilesPath = zipOpener(paramModel.getZipFile());
		ArrayList<File> unzipedFilesList = new ArrayList<>(unzipedFilesChecker(unzipedFilesPath,
														   paramModel.getStartDate(),
														   paramModel.getNumberOfDays()));

		return parser(unzipedFilesList, paramModel.getSearchQuery());
	}
}
