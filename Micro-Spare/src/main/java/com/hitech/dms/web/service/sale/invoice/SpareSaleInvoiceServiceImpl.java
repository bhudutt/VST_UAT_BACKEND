package com.hitech.dms.web.service.sale.invoice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hitech.dms.app.exceptions.docs.FileStorageException;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.common.CommonDaoImpl;
import com.hitech.dms.web.dao.spare.sale.invoice.SpareSaleInvoiceDao;
import com.hitech.dms.web.dao.spare.sale.invoice.SpareSaleInvoiceDaoImpl;
import com.hitech.dms.web.entity.spare.sale.invoice.SaleInvoiceDtlEntity;
import com.hitech.dms.web.model.SpareModel.SparePoHDRAndPartDetailsModel;
import com.hitech.dms.web.model.SpareModel.partSerachDetailsByPohdrIdResponseModel;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spare.branchTransfer.issue.response.BranchSpareIssueBinStockResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.sale.invoice.SalesInvoiceDCResponse;
import com.hitech.dms.web.model.spare.sale.invoice.request.CustomerOrderOrDCRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.SpareSalesInvoiceRequest;
import com.hitech.dms.web.model.spare.sale.invoice.request.TaxDetailsRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.PartTaxCalCulationResponse;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.model.spare.search.response.SparePoHdrDetailsResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpareSaleInvoiceServiceImpl implements SpareSaleInvoiceService {
	private static final Logger logger = LoggerFactory.getLogger(SpareSaleInvoiceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	SpareSaleInvoiceDao spareSaleInvoiceDao;

	@Autowired
	CommonDao commonDao;

	private Path fileStorageLocation;

	@Value("${file.upload-dir.InvoiceQRCodeUpload}")
	private String uploadDir;

	@Override
	public HashMap<BigInteger, String> fetchReferenceDocList(Integer dealerId, String userCode) {
		return spareSaleInvoiceDao.fetchReferenceDocList(dealerId, userCode);
	}

	@Override
	public ApiResponse<SparePoHDRAndPartDetailsModel> fetchPoHdrAndPartDetails(String userCode, Integer poHdrId) {
		ApiResponse<SparePoHDRAndPartDetailsModel> apiResponse = new ApiResponse<>();
		SparePoHDRAndPartDetailsModel responseModel = new SparePoHDRAndPartDetailsModel();
		ApiResponse<List<SparePoHdrDetailsResponse>> hdrDetailsResponse = spareSaleInvoiceDao
				.fetchPoHdrDetailsById(userCode, poHdrId);
		List<partSerachDetailsByPohdrIdResponseModel> partDetailsResponse = spareSaleInvoiceDao
				.fetchPoPartDetailsByPohdrId(userCode, poHdrId);
		responseModel.setHdrDetailsResponse(hdrDetailsResponse.getResult());
		responseModel.setPartDetailsResponse(partDetailsResponse);
		apiResponse.setResult(responseModel);
		apiResponse.setMessage(hdrDetailsResponse.getMessage());
		apiResponse.setStatus(hdrDetailsResponse.getStatus());
		return apiResponse;
	}

	@Override
	@Transactional
	public SpareSalesInvoiceResponse createSpareSaleInvoice(String userCode,
			SpareSalesInvoiceRequest salesInvoiceRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug(" invoked.." + userCode);
//			logger.debug(salesInvoiceRequest.toString());
		}
		String msg = null;
		int statusCode = 0;
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
		Map<String, Object> mapData = null;
		boolean isSuccess = true;

		BigInteger id = null;

		spareSalesInvoiceResponse = spareSaleInvoiceDao.createSpareSaleInvoice(userCode, salesInvoiceRequest);
		BigInteger saleInvoiceId = spareSalesInvoiceResponse.getId();

//				ref_doc_id	ref_doc_name
//				1	COUNTER SALE
//				2	CUSTOMER ORDER
//				3	DELIVERY CHALLAN
//				4	DEALER PO
//				5	CO-DEALER PO
//				6	CO-DISTRIBUTOR PO

		if (spareSalesInvoiceResponse.getStatusCode() == 201) {
			BigInteger referenceDocId = salesInvoiceRequest.getReferenceDocumentId();

			boolean isCustomerOrder = referenceDocId.equals(BigInteger.valueOf(2));
			boolean isDcOrder = referenceDocId.equals(BigInteger.valueOf(3));

			boolean isPoOrder = referenceDocId.equals(BigInteger.valueOf(4))
					|| referenceDocId.equals(BigInteger.valueOf(5)) || referenceDocId.equals(BigInteger.valueOf(6));

			if (isPoOrder) {
				spareSaleInvoiceDao.updateInPoHdr("PO", salesInvoiceRequest.getPoHdrId(), userCode);
			}
			String flag = isCustomerOrder ? "CO" : isDcOrder ? "DC" : null;

			for (PartDetailRequest partDetailRequest : salesInvoiceRequest.getPartDetails()) {

				if (isCustomerOrder || isDcOrder) {
					spareSaleInvoiceDao.updateInOrderStatus(flag, partDetailRequest.getCustomerOrderHdrId(), userCode);
				}
			}
		} else {
			isSuccess = false;
		}

		if (isSuccess) {
			spareSalesInvoiceResponse.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = spareSalesInvoiceResponse.getMsg();
			spareSalesInvoiceResponse.setMsg(msg);
		} else {
			spareSalesInvoiceResponse.setStatusCode(statusCode);
			spareSalesInvoiceResponse.setMsg(msg);
		}

		return spareSalesInvoiceResponse;
	}

	@Override
	public List<SalesInvoiceDCResponse> deliveryChallanDtl(Integer partyBranchId, String reqType) {
		return spareSaleInvoiceDao.deliveryChallanDtl(partyBranchId, reqType);
	}

	@Override
	public List<COpartDetailResponse> getDcPartDetail(String deliveryChallanNumber) {
		return spareSaleInvoiceDao.getDcPartDetail(deliveryChallanNumber);
	}

	@Override
	public HashMap<BigInteger, String> searchInvoiceList(String searchText, String userCode) {
		return spareSaleInvoiceDao.searchInvoiceList(searchText, userCode);
	}

	@Override
	public List<SpareSalesInvoiceResponse> fetchInvoiceList(String invoiceNumber, Date fromDate, Date toDate,
			String userCode, Integer page, Integer size) {
		return spareSaleInvoiceDao.fetchInvoiceList(invoiceNumber, fromDate, toDate, userCode, page, size);
	}

	@Override
	public HashMap<BigInteger, String> searchDCNumber(String searchText, String categoryCode, String userCode) {
		return spareSaleInvoiceDao.searchDCNumber(searchText, categoryCode, userCode);
	}

	@Override
	public SpareSalesInvoiceResponse fetchHdrAndDtl(BigInteger invoiceId, String userCode) {
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = new SpareSalesInvoiceResponse();
		spareSalesInvoiceResponse = spareSaleInvoiceDao.fetchHdr(invoiceId);

		if (!spareSalesInvoiceResponse.getIsQRCodeGenerated() && spareSalesInvoiceResponse.getQRCodeText() != null) {
			try {
				generateQRCodeImage(spareSalesInvoiceResponse.getQRCodeText(), invoiceId, userCode);

			} catch (Exception e) {
				logger.error(this.getClass().getName(), e);
			}
		}

			//spareSalesInvoiceResponse.setSaleInvoiceDtl(spareSaleInvoiceDao.fetchDtl(invoiceId, 2));
		return spareSalesInvoiceResponse;
	}

//	public byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
//		QRCodeWriter qrCodeWriter = new QRCodeWriter();
//		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//		byte[] pngData = pngOutputStream.toByteArray();
//		return pngData;
//	}

	public void generateQRCodeImage(String qrText, BigInteger invoiceId, String userCode)
			throws IOException, WriterException {
		int width = 250;
		int height = 250;

		String fileName = invoiceId.toString();
		// Generate a unique file name if not provided
		if (fileName == null || fileName.isEmpty()) {
			fileName = UUID.randomUUID().toString();
		}

		// Get the upload directory path

		Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName + ".png");

		// Create directory if it doesn't exist
//		Files.createDirectories(filePath.getParent());

		// Write QR code image to file
//		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

//		Path uploadFileDir = Paths.get(uploadDir).toAbsolutePath().normalize(); // Replace with your upload directory path
//		Path filePath = uploadFileDir.resolve(fileName + ".png"); // Append the file name to the directory path

		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height);

			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

			// Optionally, you can check if the file was created successfully
			if (Files.exists(filePath)) {
				logger.info("QR Code image successfully created at: " + filePath.toString());
				spareSaleInvoiceDao.updateQrCodeStatus(invoiceId, userCode);
			} else {
				logger.info("Error creating QR Code image");
			}
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
	}

//	public byte[] generateQRCode(VisitorDataForMail visitor) {
//		System.out.println("data" + visitor);
//		String data = constructDataString(visitor);
//
//		try {
//			int size = 300;
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
//
//			BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
//			for (int x = 0; x < size; x++) {
//				for (int y = 0; y < size; y++) {
//					bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
//				}
//			}
//
//			ImageIO.write(bufferedImage, "png", baos);
//			return baos.toByteArray();
//		} catch (Exception e) {
//			// Handle exceptions (e.g., WriterException, IOException)
//			e.printStackTrace();
//			return null;
//		}
//	}

	@Override
	public SpareSalesInvoiceResponse cancelInvoice(SaleInvoiceCancelRequest saleInvoiceCancelRequest, String userCode) {
		return spareSaleInvoiceDao.spareCancelInvoice(saleInvoiceCancelRequest, userCode);
	}

	@Override
	public PartTaxCalCulationResponse fetchTaxDetails(TaxDetailsRequest taxDetailsRequest, String userCode) {
		return spareSaleInvoiceDao.fetchTaxDetails(taxDetailsRequest, userCode);
	}

//	@Override
//	public HashMap<BigInteger, String> fetchMRPList(Integer id, String userCode) {
//		return spareSaleInvoiceDao.fetchMRPList(id, userCode);
//	}

}
