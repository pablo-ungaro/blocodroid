package br.com.jawsys.blocodroid.db;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class CarregarXML extends DefaultHandler {

	private SAXParserFactory spf;
	private SAXParser sp;
	private XMLReader xr;
	private List<Bloco> blocos = new ArrayList<Bloco>();

	private String bairroAtual;
	private String dataAtual;
	private String nomeAtual;
	private String enderecoAtual;
	private String horaAtual;

	private Bloco blocoAtual = new Bloco();

	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public CarregarXML() throws Exception {
		/** Handling XML */
		spf = SAXParserFactory.newInstance();
		sp = spf.newSAXParser();
		xr = sp.getXMLReader();

		URL blocos2011Url = new URL(
				"http://dl.dropbox.com/u/8159675/blocodroid/blocos-2011.xml");
		xr.setContentHandler(this);
		InputStream openStream = blocos2011Url.openStream();
		InputSource inputSource = new InputSource(openStream);
		inputSource.setEncoding("UTF-8");
		xr.parse(inputSource);
	}

	public List<Bloco> listarBlocos() {
		return blocos;
	}

	private Boolean currentElement = false;
	private String currentValue = null;
	private String versao;

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = true;

		if (localName.equals("root")) {
			versao = attributes.getValue("versao");
		}
	}

	public int getVersao() {
		return Integer.parseInt(versao);
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (localName.equals("bairro")) {
			bairroAtual = currentValue;
		} else if (localName.equals("endereco")) {
			enderecoAtual = currentValue;
		} else if (localName.equals("nome")) {
			nomeAtual = currentValue;
		} else if (localName.equals("data")) {
			dataAtual = currentValue;
		} else if (localName.equals("hora")) {
			horaAtual = currentValue;
			guardaBlocoAtual();
		}

	}

	private void guardaBlocoAtual() {
		blocoAtual.setBairro(bairroAtual);
		blocoAtual.setEndereco(enderecoAtual);
		blocoAtual.setNome(nomeAtual);
		try {
			cal.setTime(formatter.parse(dataAtual));
		} catch (ParseException e) {
		}
		if (horaAtual.length() > 0) {
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaAtual));
		}
		blocoAtual.setData(cal.getTime());
		blocos.add(blocoAtual);
		blocoAtual = new Bloco();
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length).trim();
			currentElement = false;
		}

	}

}