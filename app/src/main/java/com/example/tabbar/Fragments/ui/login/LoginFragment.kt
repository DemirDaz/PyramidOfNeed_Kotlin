package com.example.tabbar.Fragments.ui.login


import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tabbar.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var pd: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pd = ProgressDialog(this.context)
        val pref = this.requireActivity().getSharedPreferences("MyPref", 0)
        val editor = pref.edit()
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
        var zauzetmejl = true
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                /*loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }*/
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {

            // OVAKO
            //http://127.0.0.1:44386/api/user/?id=demco@live.com provera za da li je mejl zauzet -- DODAJ pre registracije -- DONE
            //http://127.0.0.1:44386/api/user/?id=demco@live.com&pass=sifra - provera za user i sifra kombinaciju -- Login ,ne znam treba li mi..
           // loadingProgressBar.visibility = View.VISIBLE


            /*loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )*/
            //val string = "{'email':'demir@live.com','password':'sifraaa','fullname':'Demir','age':'23'}"
            //val bookJsonObj = JSONObject(string)
            val jsonURL  = "http://10.0.2.2:44386/api/user/"+"?id=" +usernameEditText.text.toString()


            //provera
            if(zauzetmejl) {
            doAsync {


                var result = URL(jsonURL).readText()
                //loginButton.text = result

                //val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                // val jsonArray = JSONArray(result)



                uiThread {


                    if (result != "null") {
                        val jsoni :JSONObject = JSONObject(result)

                        val sifra : Any= jsoni.get("password")
                       // loginButton.text = sifra.toString()
                        if(passwordEditText.text.toString() == sifra.toString()) {
                            usernameEditText.visibility = View.GONE
                            passwordEditText.visibility = View.GONE
                            loginButton.text = "Uspešna prijava!"+"\n" + "Možete pristupiti aplikaciji na dugme Enter."
                            loginButton.isClickable = false
                            editor.putString("email",usernameEditText.text.toString())
                            editor.putString("password",passwordEditText.text.toString())
                            editor.apply()
                        }else {
                       // loginButton.text = "prvo"
                        pd.setTitle("Greška")
                        pd.setMessage("Zauzeta email adresa!")
                        pd.show() }
                    }
                    if (result == "null") {
                        zauzetmejl = false
                        pd.setTitle("Uspešna registracija")
                        pd.setMessage("Kliknite opet za prijavu!")
                        pd.show()
                       // loginButton.text = zauzetmejl.toString() hihi debug
                        }
                    if(zauzetmejl){
                        usernameEditText.text = null
                        passwordEditText.text = null
                    }



                    //pd.dismiss()
                } } }


            if(!zauzetmejl) {
                doAsync {
                    sendPostRequest(usernameEditText.text.toString(),passwordEditText.text.toString())

                    uiThread {
                       // pd.setTitle("Dobrodošli")
                       // pd.setMessage("Uspešna registracija!")
                      //  pd.show()

                        usernameEditText.visibility = View.GONE
                        passwordEditText.visibility = View.GONE
                        loginButton.text = "Možete pristupiti aplikaciji na dugme Enter."
                        loginButton.isClickable = false
                        // loadingProgressBar.visibility = View.GONE

                    }
                }
                editor.putString("email",usernameEditText.text.toString())
                editor.putString("password",passwordEditText.text.toString())
                editor.apply()
            }

        }
    }

    fun sendPostRequest(userName:String, password:String) {

        var reqParam = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
        val mURL = URL("http://10.0.2.2:44386/api/user")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(outputStream);
            wr.write(reqParam);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $response")
            }
        }
    } }
 /*
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    } */

