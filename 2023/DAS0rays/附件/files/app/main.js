import {chromium, errors} from "playwright-chromium";

const PASSWORD = "DASCTF_flag";
(async () => {
    async function visit() {
        const page = await context.newPage();
        try {
            for (let i = 0; i < 3; i++){
                try{
                    await page.goto('http://a.o.com:8080/client/list');
                    break;
                }catch (e) {
                    console.log(e);
                }
            }
            await page.waitForTimeout(1000);
            const element = await page.isVisible('button[langtag="word-login"]');
            if (element) {
                await page.fill('input[name="username"]', 'admin');
                await page.fill('input[name="password"]', PASSWORD);
                await page.click('button[langtag="word-login"]');
            }
            await page.waitForTimeout(1000);
            await page.close();
        } catch (e) {
            if (e instanceof errors.TimeoutError){
                console.log(e);
                await page.close();
            }else{
                console.log(e);
            }     
        }
    }

    const browser = await chromium.launch({
        headless: true
    });
    const context = await browser.newContext();
    context.setDefaultTimeout(10000);

    setInterval(visit, 30000);
})();

