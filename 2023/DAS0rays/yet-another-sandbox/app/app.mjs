import express from 'express';
import path from 'path';


const __dirname = path.resolve();

const app = express();
app.use(express.json());

app.use('/asserts', express.static('asserts'));

function runInShadowRealm(code) {
    let shadowRealm = new ShadowRealm();
    let result = shadowRealm.evaluate(code);
    shadowRealm = null;
    return result;
}

app.get('/', (_, res) => {
    return res.sendFile(__dirname+'/index.html');
});

app.post('/api/run', async (req, res) => {
    try{
        let { code } = req.body;
        var msg = await runInShadowRealm(code);
    } catch(error) {
        var msg = error.toString();
    }
    res.json({"msg": msg});
});


app.listen(1337, () => {
    console.log('Server listening on port 1337');
})