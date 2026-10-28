class Gs1ProductPage {
    static async render(params) {
        function formatDate(date, locale = 'en-US') {
            if (!date) return '';
            const [year, month, day] = date.split('-').map(Number);
            return new Date(year, month - 1, day).toLocaleDateString(locale, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
        } 

        const $page = $('#gs1-product-page');
        try {
            if (!params)
                throw new Error("Empty parameters");

            if (params.error)
                throw new Error(params.error);

            document.title = params.gtin;

            $('.gs1-gtin', $page).text(params.gtin);
            if (params.variant) {
                const $tr = $('.gs1-variant', $page).show();
                $('td', $tr).text(params.variant);
            }
            if (params.lot) {
                const $tr = $('.gs1-lot', $page).show();
                $('td', $tr).text(params.lot);
            }
            if (params.serial) {
                const $tr = $('.gs1-serial', $page).show();
                $('td', $tr).text(params.serial);
            }
            if (params.productionDate) {
                const $tr = $('.gs1-production-date', $page).show();
                $('td', $tr).text(formatDate(params.productionDate));
            }
            if (params.bestBeforeDate) {
                const $tr = $('.gs1-bestbefore-date', $page).show();
                $('td', $tr).text(formatDate(params.bestBeforeDate));
            }
            if (params.expiryDate) {
                const $tr = $('.gs1-expiry-date', $page).show();
                $('td', $tr).text(formatDate(params.expiryDate));
            }
            if (params.softwareVersion) {
                const $tr = $('.gs1-software-version', $page).show();
                $('td', $tr).text(params.softwareVersion);
            }
            if (params.weight) {
                const $tr = $('.gs1-weight', $page).show();
                $('td', $tr).text(`${params.weight} Kg`);
            }

            const app = simplicite.session({ url: params.serverURL, debug: false });

            const gtinObj = app.getBusinessObject('Gs1Gtin');
            const gtinRows = await gtinObj.search({ gs1GtinIdentifier: params.gtin }, { inlineImages: [ 'gs1GtinPicture', 'gs1GtinEan13Image' ] });
            if (gtinRows.length == 1) {
                const gtinRow = gtinRows[0];
                $('.gs1-gtin-picture', $page).attr('src', gtinObj.getFieldDataURL('gs1GtinPicture', gtinRow)).show();
                $('.gs1-gtin-label', $page).text(gtinRow.gs1GtinLabel);
                $('.gs1-gtin-ean13-image', $page).attr('src', gtinObj.getFieldDataURL('gs1GtinEan13Image', gtinRow)).show();
                if (gtinRow.gs1GenDescription)
                    $('.gs1-gtin-description', $page).html(gtinRow.gs1GenDescription).show();

                const type = gtinRow.gs1GtinType;

                if (type == 'MEDICAL_DEVICE') {
                    const udiObj = app.getBusinessObject('Gs1Udi');
                    const udiRows = await udiObj.search({
                        gs1UdiGtinId__gs1GtinIdentifier: params.gtin,
                        gs1GenLot: params.lot || '',
                        gs1GenSerial: params.serial || '',
                        gs1GenProductionDate: params.productionDate || '',
                        gs1GenExpiryDate: params.expiryDate || '',
                        gs1UdiSoftwareVersion: params.softwareVersion || ''
                    }, { inlineImages: [ 'gs1GenGs1128Image' ] });
                    if (udiRows.length == 1) {
                        const udiRow = udiRows[0];
                        if (udiRow.gs1GenDescription)
                            $('.gs1-description', $page).html(udiRow.gs1GenDescription).show();
                        $('.gs1-128-image', $page).attr('src', udiObj.getFieldDataURL('gs1GenGs1128Image', udiRow)).show();
                    } else
                        throw new Error("Medical device not found");
                } else if (type == 'CONSUMER_PRODUCT') {
                    const cpgObj = app.getBusinessObject('Gs1Cpg');
                    const udiRows = await cpgObj.search({
                        gs1CpgGtinId__gs1GtinIdentifier: params.gtin,
                        gs1GenVariant: params.variant || '',
                        gs1GenLot: params.lot || '',
                        gs1GenSerial: params.serial || '',
                        gs1GenProductionDate: params.productionDate || '',
                        gs1GenBestBeforeDate: params.bestBeforeDate || '',
                        gs1GenExpiryDate: params.expiryDate || ''
                    });
                    if (udiRows.length == 1) {
                        const cpgRow = udiRows[0];
                        if (cpgRow?.gs1GenDescription)
                            $('.gs1-description', $page).html(cpgRow.gs1GenDescription).show();
                    } else
                        throw new Error("Consumer product not found");
                }
            } else
                throw new Error('GTIN not found');
        } catch (err) {
            $('.error', $page).text(err).show();
        }
    }
}