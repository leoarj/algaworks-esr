package com.algaworks.algafood.infrastructure.service.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos;
	
	@Override
	public InputStream recuperar(String nomeArquivo) {
//		InputStream streamArquivo = null;
		
		try {
			
			Path arquivoPath = getArquivoPatch(nomeArquivo);
			
			return Files.newInputStream(arquivoPath);
//			File arquivo = arquivoPath.toFile();
//			
//			streamArquivo = new FileInputStream(arquivo);
		} catch (Exception e) {
			throw new StorageException("Não foi possível recuperar arquivo.", e);
		}
		
//		return streamArquivo;
	}
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			
			Path arquivoPath = getArquivoPatch(novaFoto.getNomeArquivo());
			
			FileCopyUtils.copy(novaFoto.getInputStream(),
					Files.newOutputStream(arquivoPath));
			
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
			
			Path arquivoPath = getArquivoPatch(nomeArquivo);
			
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	private Path getArquivoPatch(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

}
