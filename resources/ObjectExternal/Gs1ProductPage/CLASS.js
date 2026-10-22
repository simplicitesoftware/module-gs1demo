class Gs1ProductPage {
    static async render(params) {
        function formatDate(date, locale = 'en-US') {
            if (!date) return '';
            const [year, month, day] = date.split('-').map(Number);
            return new Date(year, month - 1, day).toLocaleDateString(locale, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
        } 

        const $page = $('#gs1-product-page');

        try {
            document.title = params.gtin;

            $('.gtin', $page).text(params.gtin);
            if (params.lot)
                $('.lot', $page).text(params.lot);
            if (params.serial)
                $('.serial', $page).text(params.serial);
            if (params.productionDate)
                $('.productionDate', $page).text(formatDate(params.productionDate));
            if (params.expiryDate)
                $('.expiryDate', $page).text(formatDate(params.expiryDate));
            if (params.softwareVersion)
                $('.softwareVersion', $page).text(params.softwareVersion);

            const app = simplicite.session({ url: params.serverURL, debug: false });

            const gtin = app.getBusinessObject('Gs1Gtin');
            const rows = await gtin.search({ gs1GtinIdentifier: params.gtin }, { inlineImages: [ 'gs1GtinPicture' ] });
            if (rows.length == 1) {
                const row = rows[0];
                document.title = row.gs1GtinLabel;
                $('.picture', $page).attr('src', gtin.getFieldDataURL('gs1GtinPicture', row)).show();
                $('.label', $page).text(row.gs1GtinLabel);
                $('.description', $page).html(row.gs1GtinDescription);
            } else
                throw new Error('Not found');
        } catch (err) {
            $('.error', $page).text(err).show();
        }
    }
}