class Gs1ProductPage {
    static async render(params) {
        console.log(params);

        const $page = $('#product');

        const app = simplicite.session({ url: params.serverURL, debug: false });
        console.log(`Version ${app.getModuleVersion()}`);

        const udi = app.getBusinessObject('Gs1Udi');
        const md = await udi.getMetaData();
        console.log(md);

        $('.gtin', $page).html(`GTIN: ${params.gtin}`);
        $('.lot', $page).html(`Lot: ${params.lot}`);
        $('.serial', $page).html(`Serial: ${params.serial}`);
        $('.description', $page).html($('<pre/>').text(JSON.stringify(md, null, 2)));
    }
}