package es.studium;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;
import java.awt.LayoutManager;

public class ClienteFTP extends JFrame {
	private static final long serialVersionUID = 1L;
	// Campos de la cabecera parte superior
	static JTextField cab = new JTextField();
	static JTextField cab2 = new JTextField();
	static JTextField cab3 = new JTextField();
	// Campos de mensajes parte inferior
	private static JTextField campo = new JTextField();
	private static JTextField campo2 = new JTextField();
	// Botones
	JButton botonCargar = new JButton("Subir fichero");
	JButton botonDescargar = new JButton("Descargar fichero");
	JButton botonBorrar = new JButton("Eliminar fichero");
	JButton botonCreaDir = new JButton("Crear carpeta");
	JButton botonDelDir = new JButton("Eliminar carpeta");
	JButton botonSalir = new JButton("Salir");
	JPanel pnl = new JPanel(new GridLayout(3, 1));
	JPanel pnlTxt = new JPanel(new GridLayout(5, 1));
	// Lista para los datos del directorio
	static JList<String> listaDirec = new JList<String>();
	// contenedor
	private final Container c = getContentPane();
	// Datos del servidor FTP - Servidor local
	static FTPClient cliente = new FTPClient();// cliente FTP
	String servidor = "localhost";
	String user = "Antonio";
	String pasw = "Studium2019;";
	boolean login;
	static String direcInicial = "/";
	static String rutacompleta = "C:/data";
	// para saber el directorio y fichero seleccionado
	static String direcSelec = direcInicial;
	static String ficheroSelec = "";
	static String nfic = "";
	private final JButton botonRenDir = new JButton("Renombrar");
	private final JPanel panel = new JPanel((LayoutManager) null);

	public static void main(String[] args) throws IOException {
		new ClienteFTP();
	} // final del main

	public ClienteFTP() throws IOException {
		super("CLIENTE BÁSICO FTP");
		setForeground(new Color(0, 0, 0));
		getContentPane().setBackground(new Color(153, 204, 255));
		// para ver los comandos que se originan
		cliente.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		cliente.connect(servidor); // conexión al servidor
		login = cliente.login(user, pasw);
		// Se establece el directorio de trabajo actual
		cliente.changeWorkingDirectory(direcInicial);
		// Obteniendo ficheros y directorios del directorio actual
		FTPFile[] files = cliente.listFiles();
		// Construyendo la lista de ficheros y directorios
		// del directorio de trabajo actual
		llenarLista(files, direcInicial);
		campo.setEditable(false);
		campo.setForeground(new Color(0, 0, 0));
		campo.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		campo.setBackground(new Color(255, 255, 255));
		campo.setFont(new Font("Tahoma", Font.BOLD, 11));
		campo.setHorizontalAlignment(SwingConstants.CENTER);
		// preparar campos de pantalla
		campo.setText("");
		cab.setEditable(false);
		cab.setForeground(new Color(0, 0, 0));
		cab.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		cab.setBackground(new Color(255, 255, 255));
		cab.setFont(new Font("Tahoma", Font.BOLD, 11));
		cab.setHorizontalAlignment(SwingConstants.CENTER);
		cab.setText("Servidor FTP: " + servidor);
		cab2.setEditable(false);
		cab2.setForeground(new Color(0, 0, 0));
		cab2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		cab2.setBackground(new Color(255, 255, 255));
		cab2.setFont(new Font("Tahoma", Font.BOLD, 11));
		cab2.setHorizontalAlignment(SwingConstants.CENTER);
		cab2.setText("Usuario: " + user);
		cab3.setEditable(false);
		cab3.setForeground(new Color(0, 0, 0));
		cab3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		cab3.setBackground(new Color(255, 255, 255));
		cab3.setFont(new Font("Tahoma", Font.BOLD, 11));
		cab3.setHorizontalAlignment(SwingConstants.CENTER);
		cab3.setText("DIRECTORIO RAIZ: " + direcInicial);

		listaDirec.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		// Preparación de la lista
		// se configura el tipo de selección para que solo se pueda
		// seleccionar un elemento de la lista
		listaDirec.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// barra de desplazamiento para la lista
		JScrollPane barraDesplazamiento = new JScrollPane(listaDirec);
		barraDesplazamiento.setBounds(24, 107, 335, 302);
		barraDesplazamiento.setPreferredSize(new Dimension(335, 420));
		getContentPane().setLayout(null);
		pnlTxt.setBounds(64, 5, 256, 90);
		// barraDesplazamiento.setBounds(new Rectangle(5, 65, 335, 420));

		pnlTxt.add(cab);
		pnlTxt.add(cab2);
		pnlTxt.add(cab3);
		pnlTxt.add(campo);
		campo2.setEditable(false);
		campo2.setForeground(new Color(0, 0, 0));
		campo2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		campo2.setBackground(new Color(255, 255, 255));
		campo2.setFont(new Font("Tahoma", Font.BOLD, 11));
		campo2.setHorizontalAlignment(SwingConstants.CENTER);
		pnlTxt.add(campo2);
		c.add(pnlTxt);

		c.add(barraDesplazamiento);
		pnl.setBounds(24, 461, 152, 100);
		c.add(pnl);
		pnl.add(botonCargar);
		botonCargar.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonCargar.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonCargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser f;
				File file;
				f = new JFileChooser();
				// solo se pueden seleccionar ficheros
				f.setFileSelectionMode(JFileChooser.FILES_ONLY);
				// título de la ventana
				f.setDialogTitle("Selecciona el fichero a subir al servidor FTP");
				// se muestra la ventana
				int returnVal = f.showDialog(f, "Cargar");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// fichero seleccionado
					file = f.getSelectedFile();
					// nombre completo del fichero
					String archivo = file.getAbsolutePath();
					// solo nombre del fichero
					String nombreArchivo = file.getName();
					try {
						SubirFichero(archivo, nombreArchivo);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}); // Fin botón subir
		pnl.add(botonDescargar);
		botonDescargar.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonDescargar.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonDescargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String directorio = direcSelec;
				if (!direcSelec.equals("/"))
					directorio = directorio + "/";
				if (!direcSelec.equals("")) {
					DescargarFichero(directorio + ficheroSelec, ficheroSelec);
				}
			}
		}); // Fin botón descargar
		pnl.add(botonBorrar);
		botonBorrar.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonBorrar.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonBorrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String directorio = direcSelec;
				if (!direcSelec.equals("/")) {
					directorio = directorio + "/";
				}
				if (!direcSelec.equals("")) {
					BorrarFichero(directorio + ficheroSelec, ficheroSelec);
				}
			}
		});
		botonSalir.setBounds(112, 597, 143, 32);
		getContentPane().add(botonSalir);
		botonSalir.setBackground(new Color(255, 99, 71));
		botonSalir.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.setBounds(200, 461, 159, 100);

		getContentPane().add(panel);
		panel.setLayout(new GridLayout(3, 1));
		panel.add(botonCreaDir);
		botonCreaDir.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonCreaDir.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonCreaDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreCarpeta = JOptionPane.showInputDialog(null, "Introduce el nombre del directorio",
						"carpeta");
				if (!(nombreCarpeta == null)) {
					String directorio = direcSelec;
					if (!direcSelec.equals("/"))
						directorio = directorio + "/";
					// nombre del directorio a crear
					directorio += nombreCarpeta.trim(); // quita blancos a derecha y a izquierda
					try {
						if (cliente.makeDirectory(directorio)) {
							String m = nombreCarpeta.trim() + " => Se ha creado correctamente ...";
							JOptionPane.showMessageDialog(null, m);
							campo.setText(m.replaceAll("//", "/"));
							// directorio de trabajo actual
							cliente.changeWorkingDirectory(direcSelec);
							FTPFile[] ff2 = null;
							// obtener ficheros del directorio actual
							ff2 = cliente.listFiles();
							// llenar la lista
							llenarLista(ff2, direcSelec);
						} else
							JOptionPane.showMessageDialog(null, nombreCarpeta.trim() + " => No se ha podido crear ...");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} // final del if
			}
		});

		botonRenDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreCarpeta = JOptionPane.showInputDialog(null, "Introduce el nuevo nombre del directorio",
						"Nuevo nombre");
				if (!(nombreCarpeta == null)) {

					if (!direcSelec.equals("/")) {

						String nufi = direcSelec.substring(0, direcSelec.length() - 1);

						File archivo = new File(rutacompleta + nufi);
						if (archivo.exists()) {
							if (archivo.isFile()) {
								System.out.println("Es un archivo");
								try {
									System.out.println("antiguo: " + direcSelec + " nuevo: " + nombreCarpeta);

									boolean success = cliente.rename(direcSelec, nombreCarpeta);

									if (success) {

										System.out
												.println("Se ha cambiado el nombre al fichero '" + nombreCarpeta + "'");
									} else {
										System.out.println("Error al intentar renombrar el fichero ubicado en: "
												+ direcSelec + " a " + nombreCarpeta);
									}

								} catch (IOException ex) {
									ex.printStackTrace();
								}
							} else if (archivo.isDirectory()) {
								System.out.println("Es una carpeta");
								try {
									System.out.println("antiguo: " + direcSelec + " nuevo: " + nombreCarpeta);

									cliente.changeToParentDirectory();
									String cpwd = cliente.printWorkingDirectory();

									boolean success = cliente.rename(direcSelec, nombreCarpeta);

									if (success) {

										System.out.println(
												"Se ha cambiado el nombre del directorio a '" + nombreCarpeta + "'");
										System.out.println("cpwd vale'" + cpwd + "'");
										cliente.changeWorkingDirectory(cpwd);
										FTPFile[] ff2 = null;
										// obtener ficheros del directorio actual
										ff2 = cliente.listFiles();
										// llenar la lista con los ficheros del directorio actual
										llenarLista(ff2, cpwd);
									} else {
										System.out.println("Error al intentar renombrar el directorio ubicado en: "
												+ direcSelec + " a " + nombreCarpeta);
									}

								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
						} else {
							System.out.println("EL ARCHIVO NO EXISTE");
						}

					} // final del if
				}
			}

		});
		panel.add(botonDelDir);
		botonDelDir.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonDelDir.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonDelDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreCarpeta = JOptionPane.showInputDialog(null,
						"Introduce el nombre del directorio a eliminar", "carpeta");
				if (!(nombreCarpeta == null)) {
					String directorio = direcSelec;
					if (!direcSelec.equals("/"))
						directorio = directorio + "/";
					// nombre del directorio a eliminar
					directorio += nombreCarpeta.trim(); // quita blancos a derecha y a izquierda
					try {
						if (cliente.removeDirectory(directorio)) {
							String m = nombreCarpeta.trim() + " => Se ha eliminado correctamente ...";
							JOptionPane.showMessageDialog(null, m);
							campo.setText(m.replaceAll("//", "/"));
							// directorio de trabajo actual
							cliente.changeWorkingDirectory(direcSelec);
							FTPFile[] ff2 = null;
							// obtener ficheros del directorio actual
							ff2 = cliente.listFiles();
							// llenar la lista
							llenarLista(ff2, direcSelec);
						} else
							JOptionPane.showMessageDialog(null,
									nombreCarpeta.trim() + " => No se ha podido eliminar ...");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} // final del if
			}
		}); // final del botón Eliminar Carpeta
		panel.add(botonRenDir);
		botonRenDir.setFont(new Font("Tahoma", Font.BOLD, 11));
		botonRenDir.setBackground(UIManager.getColor("InternalFrame.inactiveTitleGradient"));
		botonSalir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cliente.disconnect();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		// se añaden el resto de los campos de pantalla
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 690);
		setLocationRelativeTo(this);
		setVisible(true);
		// Acciones al pulsar en la lista o en los botones
		listaDirec.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent lse) {
				
				String fic = "";
				
				if (lse.getValueIsAdjusting()) {
					ficheroSelec = "";
					// elemento que se ha seleccionado de la lista
					
					fic = listaDirec.getSelectedValue().toString();
					//Si fic no esta vacio le asignamos el valor a nfic
					if (!fic.isEmpty())
					{
						nfic = fic;
					}
	
				}
				if (listaDirec.getSelectedIndex() == 0) {
					// Se hace clic en el primer elemento del JList
					// se comprueba si es el directorio inicial
					if (!fic.equals(direcInicial)) {
						// si no estamos en el directorio inicial, hay que
						// subir al directorio padre
						try {
							cliente.changeToParentDirectory();
							direcSelec = cliente.printWorkingDirectory();
							FTPFile[] ff2 = null;
							// directorio de trabajo actual=directorio padre
							cliente.changeWorkingDirectory(direcSelec);
							// se obtienen ficheros y directorios
							ff2 = cliente.listFiles();
							campo.setText("");
							// se llena la lista con ficheros del directorio padre
							llenarLista(ff2, direcSelec);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				// No se hace clic en el primer elemento del JList
				// Puede ser un fichero o un directorio
				else {
					File nf = new File(direcSelec);
					if (fic.contains("(DIR)")) {
						// Se trata de un directorio
						try {
							fic = fic.substring(6);
							String direcSelec2 = "";
							if (direcSelec.equals("/"))
							{
								System.out.println("----------------direcSelec vale "+direcSelec);
								direcSelec2 = direcSelec + fic;
								direcSelec2.replaceAll("//", "/");
							}
							else
							{
								System.out.println("----------------direcSelec vale "+direcSelec);
								direcSelec2 = direcSelec + "/" + fic;
								direcSelec2.replaceAll("//", "/");
							}
							FTPFile[] ff2 = null;
							cliente.changeWorkingDirectory(direcSelec2);
							ff2 = cliente.listFiles();
							fic=fic.replaceAll("//", "/");
							campo.setText("DIRECTORIO: " + fic + ", " + ff2.length + " elementos");
							// directorio actual
							direcSelec = direcSelec2;
							// se llena la lista con datos del directorio
							llenarLista(ff2, direcSelec);
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					} else {
						// Se trata de un fichero

						ficheroSelec = direcSelec;
						if (direcSelec.equals("/")) {

							direcSelec += fic;
						} else {

							direcSelec += "/" + fic;
							System.out.println("fic: " + fic);
							System.out.println("nfic: " + nfic);
							System.out.println("direcSelec+fic: " + direcSelec);
						}
						campo.setText("FICHERO SELECCIONADO: " + ficheroSelec.replaceAll("//", "/"));
						ficheroSelec = fic;// nos quedamos con el nocmbre

					} // fin else
				} // fin else de fichero o directorio
				campo2.setText("DIRECTORIO ACTUAL: " + direcSelec.replaceAll("//", "/"));
				// fin if inicial
			}
		});// fin acción en JList
	} // fin constructor

	private static void llenarLista(FTPFile[] files, String direc2) {
		if (files == null)
			return;
		// se crea un objeto DefaultListModel
		DefaultListModel<String> modeloLista = new DefaultListModel<String>();
		modeloLista = new DefaultListModel<String>();
		// se definen propiedades para la lista, color y tipo de fuente
		listaDirec.setForeground(new Color(51, 51, 51));
		listaDirec.setFont(new Font("Consolas", Font.BOLD, 12));
		// se eliminan los elementos de la lista
		listaDirec.removeAll();
		try {
			cliente.changeWorkingDirectory(direc2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		direcSelec = direc2; // directorio actual
		// se añade el directorio de trabajo al listmodel, primer elemento
		modeloLista.addElement(direc2);
		// se recorre el array con los ficheros y directorios
		for (int i = 0; i < files.length; i++) {
			if (!(files[i].getName()).equals(".") && !(files[i].getName()).equals("..")) {
				// nos saltamos los directorios . y ..
				// Se obtiene el nombre del fichero o directorio
				String f = files[i].getName();
				// Si es directorio se añade al nombre (DIR)
				if (files[i].isDirectory())
					f = "(DIR) " + f;
				// se añade el nombre del fichero o directorio al listmodel
				modeloLista.addElement(f);
			} // fin if
		} // fin for
		try {
			// se asigna el listmodel al JList,
			// se muestra en pantalla la lista de ficheros y direc
			listaDirec.setModel(modeloLista);
		} catch (NullPointerException n) {
			// Se produce al cambiar de directorio
		}
	}// Fin llenarLista

	private boolean SubirFichero(String archivo, String soloNombre) throws IOException {
		cliente.setFileType(FTP.BINARY_FILE_TYPE);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(archivo));
		boolean ok = false;
		// directorio de trabajo actual
		cliente.changeWorkingDirectory(direcSelec);
		if (cliente.storeFile(soloNombre, in)) {
			String s = " " + soloNombre + " => Subido correctamente...";
			campo.setText(s);
			campo2.setText("Se va a actualizar el árbol de directorios...");
			JOptionPane.showMessageDialog(null, s);
			FTPFile[] ff2 = null;
			// obtener ficheros del directorio actual
			ff2 = cliente.listFiles();
			// llenar la lista con los ficheros del directorio actual
			llenarLista(ff2, direcSelec);
			ok = true;
		} else
			campo.setText("No se ha podido subir... " + soloNombre);
		return ok;
	}// final de SubirFichero

	private void DescargarFichero(String NombreCompleto, String nombreFichero) {
		File file;
		// le quitamos la ultima "/" para que quede solo el nombre del fichero
		
		String nufi = direcSelec.substring(0, direcSelec.length() - 1);
		String archivoyCarpetaDestino = "";
		String carpetaDestino = "";
		System.out.println("***********DIRECSELEC 1 "+direcSelec);
		System.out.println("***********NUFI "+nufi);
		System.out.println("***********Nomfi "+nombreFichero);
		System.out.println("***********Nomcom "+NombreCompleto);
		
		
		JFileChooser f = new JFileChooser();
		// solo se pueden seleccionar directorios
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// ttulo de la ventana
		f.setDialogTitle("Selecciona el Directorio donde Descargar el Fichero");
		int returnVal = f.showDialog(null, "Descargar");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = f.getSelectedFile();
			// obtener carpeta de destino
			carpetaDestino = (file.getAbsolutePath()).toString();
			// construimos el nombre completo que se crear en nuestro disco
			archivoyCarpetaDestino = carpetaDestino + File.separator + nfic;
			System.out.println("***********carpeta destino "+carpetaDestino);
			System.out.println("***********archivo + carpetadestino "+archivoyCarpetaDestino);

			try {
				cliente.setFileType(FTP.BINARY_FILE_TYPE);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(archivoyCarpetaDestino));
				if (cliente.retrieveFile(NombreCompleto, out))
					JOptionPane.showMessageDialog(null,
							nufi + " => Se ha descargado correctamente en '" + archivoyCarpetaDestino + "'");
				else
					JOptionPane.showMessageDialog(null, nufi + " => No se ha podido descargar ...");
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	} // Final de DescargarFichero

	private void BorrarFichero(String NombreCompleto, String nombreFichero) {
		// pide confirmación
		String nufi = direcSelec.substring(0, direcSelec.length() - 1);
		File archivo = new File(rutacompleta + nufi);
		int seleccion = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el fichero '" + archivo.getName() + "'?");
		if (seleccion == JOptionPane.OK_OPTION) {
			try {

				if (archivo.exists()) {
					System.out.println("Nombre del archivo " + archivo.getName());

					System.out.println("NombreFichero -->" + nufi);
					if (cliente.deleteFile(NombreCompleto)) {
						String m = archivo.getName() + " => Eliminado correctamente... ";
						JOptionPane.showMessageDialog(null, m);
						campo.setText(m);
						// directorio de trabajo actual

						
						//cojer solo la ruta relativa del archivo
						String niu = direcSelec.substring(0, (direcSelec.length()) - (archivo.getName().length() + 1));
						System.out.println("****** NIU: "+niu);
						//le quitamos las barras dobles
						niu.replaceAll("//", "/");
						FTPFile[] ff2 = null;
						// obtener ficheros del directorio actual
						ff2 = cliente.listFiles();
						// llenar la lista con los ficheros del directorio donde estaba ubicado el archivo
						llenarLista(ff2, niu);
					} else
						JOptionPane.showMessageDialog(null,"No se ha podido eliminar '"+archivo.getName()+"'...");
				} else {
					System.out.println("El archivo " + nufi + " no existe");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}// Final de BorrarFichero
}// Final de la clase ClienteFTPBasico