/*
 * 
 */

package com.rapidminer.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import com.rapidminer.ProcessLocation;
import com.rapidminer.ProcessStorageListener;
import com.rapidminer.gui.actions.Actions;
import com.rapidminer.gui.actions.RunAction;
import com.rapidminer.gui.actions.SaveAction;
import com.rapidminer.gui.actions.ToggleAction;
import com.rapidminer.gui.operatortree.OperatorTree;
import com.rapidminer.gui.processeditor.ExtendedProcessEditor;
import com.rapidminer.gui.processeditor.NewOperatorEditor;
import com.rapidminer.gui.processeditor.ProcessContextProcessEditor;
import com.rapidminer.gui.processeditor.ProcessEditor;
import com.rapidminer.gui.processeditor.results.ResultDisplay;
import com.rapidminer.gui.properties.OperatorPropertyPanel;
import com.rapidminer.gui.tools.StatusBar;
import com.rapidminer.gui.tools.WelcomeScreen;
import com.rapidminer.gui.tools.dialogs.wizards.dataimport.DataImportWizardFactory;
import com.rapidminer.gui.tools.dialogs.wizards.dataimport.DataImportWizardRegistry;
import com.rapidminer.gui.tools.logging.LogViewer;
import com.rapidminer.gui.tour.comic.states.ComicRenderer;
import com.rapidminer.operator.Operator;
import com.rapidminer.repository.gui.RepositoryBrowser;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingDesktop;

/**
 * The interface providing the functionality of the UI for RapidMiner.
 * 
 * @author Gabor Bakos
 */
public interface MainUIState extends MenusUI, ProcessState {
	public void finishInitialization();

	public void setExpertMode(boolean expert);

	public OperatorPropertyPanel getPropertyPanel();

	/**
	 * Returns a registry for {@link DataImportWizardFactory} instances. The factories are used to
	 * populate menus such as the main import menu.
	 *
	 * @return the registry
	 */
	public DataImportWizardRegistry getDataImportWizardRegistry();
	
	public LogViewer getLogViewer();

	public NewOperatorEditor getNewOperatorEditor();

	public OperatorTree getOperatorTree();

	public Actions getActions();

	public ResultDisplay getResultDisplay();

	public WelcomeScreen getWelcomeScreen();

	// /** Updates the list of recently used files. */
	// public void updateRecentFileList();

	/*
	 * public void windowOpened(WindowEvent e);
	 * 
	 * public void windowClosing(WindowEvent e);
	 * 
	 * public void windowClosed(WindowEvent e);
	 * 
	 * public void windowIconified(WindowEvent e);
	 * 
	 * public void windowDeiconified(WindowEvent e);
	 * 
	 * public void windowActivated(WindowEvent e);
	 * 
	 * public void windowDeactivated(WindowEvent e);
	 */

	public List<Operator> getSelectedOperators();

	public Operator getFirstSelectedOperator();

	public void selectOperator(Operator currentlySelected);

	public void selectOperators(List<Operator> currentlySelected);

	public DockingDesktop getDockingDesktop();

	public Perspectives getPerspectives();

	public void handleBrokenProxessXML(ProcessLocation location, String xml,
										Exception e);

	public OperatorDocumentationBrowser getOperatorDocViewer();

	public ComicRenderer getComicRenderer();

	public void registerDockable(Dockable dockable);

	public ProcessContextProcessEditor getProcessContextEditor();

	RepositoryBrowser getRepositoryBrowser();

	public Component getXMLEditor();

	/**
	 * Returns the status bar of the application.
	 * 
	 * @return status bar
	 */
	public StatusBar getStatusBar();

	/**
	 * Closes the current process
	 *
	 * @param askForConfirmation
	 *            if <code>true</code>, will prompt the user if he really wants to close the current
	 *            process
	 * @return Saved?
	 */
	public boolean close(final boolean askForConfirmation);

	/**
	 * @return Saved?
	 */
	boolean close();

	/**
	 * @param relaunch
	 */
	void exit(boolean relaunch);

	JFrame getWindow();

	Action getImportCsvFileAction();

	Action getImportExcelFileAction();

	//Removed in 6.x
//	Action getImportXmlFileAction();
//
//	Action getImportAccessFileAction();
//
//	Action getImportDatabaseTableAction();

	ToggleAction getValidateAutomaticallyAction();

	Action getPropagateRealMetadataAction();

	Action getRewireRecursively();
	
	//Removed in 6.x
//	RunRemoteNowAction getRunRemoteNowAction();

	RunAction getRunAction();
	
	SaveAction getSaveAction();

	ToggleAction getToggleExpertModeAction();

	/**
	 * @deprecated use {@link #addExtendedProcessEditor(ExtendedProcessEditor)} instead.
	 */
	@Deprecated
	public void addProcessEditor(final ProcessEditor p);

	/**
	 * Adds the given {@link ExtendedProcessEditor} listener.
	 *
	 * @param p
	 */
	public void addExtendedProcessEditor(final ExtendedProcessEditor p);

	/**
	 * @deprecated use {@link #removeExtendedProcessEditor(ExtendedProcessEditor)} instead.
	 */
	@Deprecated
	public void removeProcessEditor(final ProcessEditor p);

	/**
	 * Removes the given {@link ExtendedProcessEditor} listener.
	 *
	 * @param p
	 */
	public void removeExtendedProcessEditor(final ExtendedProcessEditor p);
	
	void removeProcessStorageListener(ProcessStorageListener listener);

	void addProcessStorageListener(ProcessStorageListener listener);

	void addViewSwitchToUndo();

	DockableMenu getDockableMenu();
	
	/**
	 *
	 * @return the toolbar containg e.g. process run buttons
	 */
	public JToolBar getButtonToolbar();
}