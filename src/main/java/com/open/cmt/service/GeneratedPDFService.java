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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratedPDFService {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public byte[] generarPDFSolicitud(Solicitud solicitud, Incidente incidente) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float marginLeft = 75;
            float marginRight = PDRectangle.A4.getWidth() - 75;
            float marginTop = PDRectangle.A4.getHeight() - 60;
            float marginBottom = 60;
            float lineHeight = 15;

            String formattedDate = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(dateFormatter) : null;
            String formattedTime = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(timeFormatter) : null;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

                float yPosition = marginTop - 2 * lineHeight;

                // Encabezado con número de solicitud a la izquierda y fecha a la derecha
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, marginTop - lineHeight);
                contentStream.showText("Solicitud N°: " + solicitud.getNroSolicitud());
                contentStream.newLineAtOffset(marginRight - 170, 0);
                contentStream.showText(formattedDate + " " + formattedTime);
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

                yPosition -= 2 * lineHeight;
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

                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Dirección:");
                contentStream.endText();

                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.showText(incidente.getDireccion());
                contentStream.endText();

                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.showText("Detalle:");
                contentStream.endText();

                yPosition -= lineHeight;
                yPosition = addWrappedText(contentStream, incidente.getDetalle(), new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12, marginLeft, marginRight, yPosition, lineHeight);

                /// Listado de Personal y Cargos
                yPosition -= 2 * lineHeight;
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.showText("Personal");
                contentStream.newLineAtOffset(marginRight - 150, 0);
                contentStream.showText("Cargo");
                contentStream.endText();

                yPosition -= lineHeight;
                if (incidente.getPersonalList().isEmpty()) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    contentStream.showText("-");
                    contentStream.newLineAtOffset(marginRight - 150, 0);
                    contentStream.showText("-");
                    contentStream.endText();
                    yPosition -= lineHeight;
                } else {
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
                if (incidente.getVehiculos().isEmpty()) {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    contentStream.showText("-");
                    contentStream.newLineAtOffset(marginRight - 150, 0);
                    contentStream.showText("-");
                    contentStream.endText();
                    yPosition -= lineHeight;
                } else {
                    for (Vehiculo vehiculo : incidente.getVehiculos()) {
                        contentStream.beginText();
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                        contentStream.newLineAtOffset(marginLeft, yPosition);
                        contentStream.showText("Movil " + vehiculo.getNumero());
                        contentStream.newLineAtOffset(marginRight - 150, 0);
                        contentStream.showText(vehiculo.getPlaca());
                        contentStream.endText();
                        yPosition -= lineHeight;
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();
            return outputStream.toByteArray();
        }
    }

    public float addWrappedText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float marginLeft, float marginRight, float yPosition, float lineHeight) throws IOException {
        float maxWidth = marginRight - marginLeft; // Ancho disponible entre los márgenes

        // Divide el texto en líneas ajustadas al ancho máximo
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        List<String> lineWords = new ArrayList<>();

        for (String word : words) {
            String testLine = line + word + " ";
            float textWidth = font.getStringWidth(testLine) / 1000 * fontSize;

            if (textWidth > maxWidth) {
                // Justifica la línea actual
                if (lineWords.size() > 1) {
                    float lineWidth = font.getStringWidth(line.toString().trim()) / 1000 * fontSize;
                    float extraSpace = (maxWidth - lineWidth) / (lineWords.size() - 1);

                    float currentX = marginLeft;
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(currentX, yPosition);

                    for (int i = 0; i < lineWords.size(); i++) {
                        String wordInLine = lineWords.get(i);
                        contentStream.showText(wordInLine);

                        // Añade el espacio extra entre palabras (excepto después de la última palabra)
                        if (i < lineWords.size() - 1) {
                            currentX += font.getStringWidth(wordInLine + " ") / 1000 * fontSize + extraSpace;
                            contentStream.newLineAtOffset(extraSpace + font.getStringWidth(wordInLine + " ") / 1000 * fontSize, 0);
                        }
                    }
                    contentStream.endText();
                } else {
                    // Si solo hay una palabra en la línea, simplemente alinéala a la izquierda
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    contentStream.showText(line.toString().trim());
                    contentStream.endText();
                }

                // Actualiza la posición vertical
                yPosition -= lineHeight;
                line = new StringBuilder();
                lineWords.clear();
            }

            line.append(word).append(" ");
            lineWords.add(word);
        }

        // Dibuja cualquier texto restante en el buffer sin justificar (última línea)
        if (!line.isEmpty()) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(marginLeft, yPosition);
            contentStream.showText(line.toString().trim());
            contentStream.endText();

            yPosition -= lineHeight;
        }

        yPosition -= lineHeight;

        return yPosition;
    }
}
