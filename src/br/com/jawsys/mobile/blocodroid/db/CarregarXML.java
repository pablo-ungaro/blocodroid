/**
 * This file is part of BlocoDroid.
 *
 *  BlocoDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BlocoDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with BlocoDroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.jawsys.mobile.blocodroid.db;

import java.io.InputStream;
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
	private List<Bloco> listaBlocos = new ArrayList<Bloco>();

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
		xr.setContentHandler(this);

		/*
		 * URL blocos2011Url = new URL(
		 * "http://dl.dropbox.com/u/8159675/blocodroid/blocos.zip");
		 */
		InputStream openStream = getClass().getClassLoader()
				.getResourceAsStream("blocos.xml");
		// ZipInputStream zis = new ZipInputStream(openStream);
		// zis.getNextEntry();
		InputSource inputSource = new InputSource(openStream);
		inputSource.setEncoding("UTF-8");
		xr.parse(inputSource);
		// zis.close();
		openStream.close();
	}

	public List<Bloco> listarBlocos() {
		return listaBlocos;
	}

	private Boolean currentElement = false;
	private String currentValue = null;
	private String versao;
	private boolean hasCharacters;

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentElement = true;
		hasCharacters = false;
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

		if (localName.equals("b")) {
			bairroAtual = hasCharacters ? currentValue : null;
		} else if (localName.equals("e")) {
			enderecoAtual = hasCharacters ? currentValue : null;
		} else if (localName.equals("n")) {
			nomeAtual = hasCharacters ? currentValue : null;
		} else if (localName.equals("d")) {
			dataAtual = hasCharacters ? currentValue : null;
		} else if (localName.equals("h")) {
			horaAtual = hasCharacters ? currentValue : null;
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
		if (horaAtual != null && horaAtual.trim().length() > 0) {
			try {
				int parseInt = Integer.parseInt(horaAtual);
				cal.set(Calendar.HOUR_OF_DAY, parseInt);
			} catch (NumberFormatException e) {
			}
		}
		blocoAtual.setData(cal.getTime());
		listaBlocos.add(blocoAtual);
		blocoAtual = new Bloco();
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		hasCharacters = true;
		if (currentElement) {
			currentValue = new String(ch, start, length).trim();
			currentElement = false;
		}

	}

}
