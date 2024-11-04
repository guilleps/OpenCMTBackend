package com.open.cmt.service;

import com.open.cmt.entity.Incidente;
import com.open.cmt.entity.Personal;
import com.open.cmt.entity.Solicitud;
import com.open.cmt.entity.Vehiculo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GeneratedPDFService {

    public byte[] generarPDFSolicitud(Solicitud solicitud, Incidente incidente) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float marginLeft = 75;
            float marginRight = PDRectangle.A4.getWidth() - 75;
            float marginTop = PDRectangle.A4.getHeight() - 60;
            float marginBottom = 60;
            float lineHeight = 15;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

                float yPosition = marginTop - 2 * lineHeight;

                // Encabezado con número de solicitud a la izquierda y fecha a la derecha
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, marginTop - lineHeight);
                contentStream.showText("Solicitud N°: " + solicitud.getNroSolicitud());
                contentStream.newLineAtOffset(marginRight - 175, 0);
                contentStream.showText(solicitud.getFechaHora().toString());
                contentStream.endText();

                yPosition -= 2 * lineHeight;

                // Detalles de la solicitud
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.showText("Tipo de Incidente:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getTipoIncidente().getNombre());
                contentStream.endText();

                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Zona:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getZona().getNombre());
                contentStream.endText();

                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Sector:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getSector().getTitulo());
                contentStream.endText();

                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Tipo de Intervención:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getTipoIntervencion());
                contentStream.endText();

                yPosition -= 3 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Resultado de la Intervención:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getResultado());
                contentStream.endText();

                yPosition -= 3 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Detalle:");
                contentStream.endText();

                yPosition -= lineHeight;
                addWrappedText(contentStream, incidente.getDetalle(), new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12, marginLeft, marginRight, yPosition, lineHeight);

                /// Listado de Personal y Cargos
                yPosition -= 3 * lineHeight;
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.showText("Personal");
                contentStream.newLineAtOffset(marginRight - 150, 0);
                contentStream.showText("Cargo");
                contentStream.endText();

                yPosition -= lineHeight;
                for (Personal personal : incidente.getPersonalList()) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    contentStream.showText(personal.getNombre());
                    contentStream.newLineAtOffset(marginRight - 150, 0);
                    contentStream.showText(personal.getCargo().getNombre());
                    contentStream.endText();
                    yPosition -= lineHeight;
                }

                // Listado de Vehículos y Placas
                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.showText("Vehículos");
                contentStream.newLineAtOffset(marginRight - 150, 0);
                contentStream.showText("Placa");
                contentStream.endText();

                yPosition -= lineHeight;
                for (Vehiculo vehiculo : incidente.getVehiculos()) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    contentStream.showText("Movil" + vehiculo.getNumero());
                    contentStream.newLineAtOffset(marginRight - 150, 0);
                    contentStream.showText(vehiculo.getPlaca());
                    contentStream.endText();
                    yPosition -= lineHeight;
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();
            return outputStream.toByteArray();
        }
    }

    public void addWrappedText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float marginLeft, float marginRight, float yPosition, float lineHeight) throws IOException {
        float maxWidth = marginRight - marginLeft; // Ancho disponible entre los márgenes
        float startX = marginLeft;

        // Divide el texto en líneas ajustadas al ancho máximo
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String testLine = line + word + " ";
            float textWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (textWidth > maxWidth) {
                // Dibuja la línea actual y salta a la siguiente
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, yPosition);
                contentStream.showText(line.toString().trim());
                contentStream.endText();

                // Actualiza la posición vertical
                yPosition -= lineHeight;
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }

        // Dibuja cualquier texto restante en el buffer
        if (line.length() > 0) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(startX, yPosition);
            contentStream.showText(line.toString().trim());
            contentStream.endText();
        }
    }

}
