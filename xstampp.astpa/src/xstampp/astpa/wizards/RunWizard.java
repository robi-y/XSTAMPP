package xstampp.astpa.wizards;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import messages.Messages;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.util.jobs.Run;
import xstampp.astpa.wizards.pages.PdfExportPage;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

public class RunWizard extends AbstractExportWizard {

	private final static String OUTPUT ="Output"; //$NON-NLS-1$
	private RunPage page;

	public RunWizard() {
		super();
		Object selection =PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ProjectExplorer.ID).getViewSite()
				.getSelectionProvider().getSelection();
		String projectName=""; //$NON-NLS-1$
		if(selection instanceof IProjectSelection){
			projectName = ProjectManager.getContainerInstance().
									getTitle(((IProjectSelection) selection).getProjectId());
		}
		File outputDir =new File(Platform.getInstanceLocation().getURL().getPath().toString()
								+ OUTPUT);
		if(!outputDir.exists()){
				outputDir.mkdirs();
		}
		this.page = new RunPage(Messages.RunExport, projectName);
		this.setExportPage(this.page);
	}

	@Override
	public boolean performFinish() {
		File outputDir =new File(this.page.getExportPath());
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		//delete/create or clear the  folders that are needed for the run export
		HashMap<File,Boolean> folders= new HashMap<>();
		folders.put(new File(outputDir + File.separator + Run.PDF_DIR), this.page.getPdfChoice());
		folders.put(new File(outputDir + File.separator + Run.CSV_DIR), this.page.getCsvChoice());
		folders.put(new File(outputDir + File.separator + Run.IMAGE_DIR), this.page.getImgChoice() ||this.page.getReportChoice());
		folders.put(new File(outputDir + File.separator + Run.EX_PDF_DIR), this.page.getXPdfChoice());
		folders.put(new File(outputDir + File.separator + Run.EX_CSV_DIR), this.page.getXCSVChoice());
		folders.put(new File(outputDir + File.separator + Run.EX_IMAGE_DIR), this.page.getXImgChoice());
		for(Entry<File, Boolean> entry: folders.entrySet()){
			if(!entry.getKey().exists() && entry.getValue()){
				entry.getKey().mkdirs();
			}else if(entry.getKey().exists()){
				for(File f: entry.getKey().listFiles()){
					f.delete();
				}
				if(!entry.getValue()){
					entry.getKey().delete();
				}
			}
		}
		
		Run runjob= new Run(this.page.getProjectName(), outputDir + File.separator,this.page.getProjectID());
		runjob.exportPDFs(this.page.getPdfData());
		runjob.exportCSVs(this.page.getCsvChoice());
		runjob.exportImages(this.page.getImgData());
		runjob.exportReport(this.page.getReportData());
		runjob.setExportExtendedCSVs(this.page.getXCSVChoice());
		runjob.setExportExtendedIMGs(this.page.getXImgData());
		runjob.setExportExtendedPDFs(this.page.getXPdfData());
		runjob.setcsDecoration(page.getDecoChoice());
		runjob.schedule();
		return true;
	}

	private class RunPage extends PdfExportPage{

		Label csvCheckbox;
		private Label xstpaCSVCheckbox;
		private Label pdfCheckbox,imgCheckbox,reportCheckbox,xstpaPDFCheckbox,xstpaIMGCheckbox;
		
		public RunPage(String pageName, String projectName) {
			super(pageName, projectName, PathComposite.DIR_DIALOG);
		}
		
		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			this.pathChooser.setText(Platform.getInstanceLocation().getURL().getFile()
					+ OUTPUT + File.separator + getProjectName());
			FormData data = new FormData();
			data.bottom = new FormAttachment(100,0);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);

			GridData checkBoxData = new GridData(SWT.FILL, SWT.FILL, true, true);
			
			final Composite exportScope = new Composite((Composite) getControl(), SWT.SHADOW_ETCHED_IN);
			
			exportScope.setLayoutData(data);
			exportScope.setLayout(new GridLayout(5,true));
			
			String tooltip="Here you can choose the available documents\n"
					+ "which you want to be included in the export\n\n"
					+ "By clicking on one of the lables the exportmode is changed";
			addLabelWithAssist(exportScope, new GridData(), "STPA content:", tooltip);
			this.pdfCheckbox=addFormatWidget(exportScope,checkBoxData, Run.PDF_DIR, true);
			
			this.imgCheckbox=addFormatWidget(exportScope,checkBoxData, Run.IMAGE_DIR, true);

			this.csvCheckbox = addFormatWidget(exportScope,checkBoxData, Run.CSV_DIR, false);

			this.reportCheckbox=addFormatWidget(exportScope,checkBoxData, Messages.STPAPDFReport, true);
			tooltip="Here you can choose the available documents created in the Context Tables\n"
					+ "which you want to be included in the export\n\n"
					+ "By clicking on one of the lables the exportmode is changed";
			
			addLabelWithAssist(exportScope, new GridData(), "extended STPA content:", tooltip);
			
			this.xstpaPDFCheckbox=addFormatWidget(exportScope,checkBoxData, Run.PDF_DIR, true);
			
			this.xstpaIMGCheckbox=addFormatWidget(exportScope,checkBoxData, Run.IMAGE_DIR, true);

			this.xstpaCSVCheckbox = addFormatWidget(exportScope,checkBoxData, Run.CSV_DIR, false);
			exportScope.layout(true);
			setPageComplete(checkFinish());
		}

		/**
		 * @return the pdfCheckbox
		 */
		public boolean getPdfChoice() {
			return !this.pdfCheckbox.getData(EXPORT_DATA).equals(NON);
		}

		/**
		 * @return the imgCheckbox
		 */
		public boolean getImgChoice() {
			return !this.imgCheckbox.getData(EXPORT_DATA).equals(NON);
		}
		/**
		 * @return the pdfCheckbox
		 */
		public String getPdfData() {
			return (String) this.pdfCheckbox.getData(EXPORT_DATA);
		}

		/**
		 * @return the imgCheckbox
		 */
		public String getImgData() {
			return (String) this.imgCheckbox.getData(EXPORT_DATA);
		}

		/**
		 * @return the csvCheckbox
		 */
		public boolean getCsvChoice() {
			return this.csvCheckbox.getData(EXPORT_DATA).equals(EXPORT);
		}
		
		/**
		 * @return the reportCheckbox
		 */
		public boolean getReportChoice() {
			return !this.reportCheckbox.getData(EXPORT_DATA).equals(NON);
		}
		/**
		 * @return the reportCheckbox
		 */
		public String getReportData() {
			return (String) this.reportCheckbox.getData(EXPORT_DATA);
		}
		/**
		 * @return the pdfCheckbox
		 */
		public boolean getXPdfChoice() {
			return !this.xstpaPDFCheckbox.getData(EXPORT_DATA).equals(NON);
		}

		/**
		 * @return the imgCheckbox
		 */
		public String getXImgData() {
			return (String) this.xstpaIMGCheckbox.getData(EXPORT_DATA);
		}
		/**
		 * @return the pdfCheckbox
		 */
		public String getXPdfData() {
			return (String) this.xstpaPDFCheckbox.getData(EXPORT_DATA);
		}

		/**
		 * @return the imgCheckbox
		 */
		public boolean getXImgChoice() {
			return !this.xstpaIMGCheckbox.getData(EXPORT_DATA).equals(NON);
		}
		
		/**
		 * @return the imgCheckbox
		 */
		public boolean getXCSVChoice() {
			return this.xstpaCSVCheckbox.getData(EXPORT_DATA).equals(EXPORT);
		}
	}
}


