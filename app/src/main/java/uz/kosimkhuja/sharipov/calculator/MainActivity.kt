package uz.kosimkhuja.sharipov.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.kosimkhuja.sharipov.calculator.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentNumber = ""
    private val actions = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // loading actions
        binding.numbersField.text = "0"
        loadOnNumberClicked()
        loadOnActionClicked()
        loadOnItemClicked()
    }

    private fun loadOnItemClicked() {
        // adding logic to clear button
        binding.clearBtn.setOnClickListener {
            currentNumber = ""
            binding.numbersField.text = "0"
            actions.clear()
        }
        // adding logic to positive/negative button
        binding.positiveNegativeBtn.setOnClickListener {
            if (currentNumber != "") {
                if (currentNumber[0].toString() == "-") {
                    var appendStr = ""
                    for (i in 1 until currentNumber.length) {
                        appendStr += currentNumber[i]
                    }
                    currentNumber = appendStr
                    binding.numbersField.text = currentNumber
                } else {
                    var appendStr = "-"
                    for (element in currentNumber) {
                        appendStr += element
                    }
                    currentNumber = appendStr
                    binding.numbersField.text = currentNumber
                }
            }
        }
        // adding logic to percent button
        binding.percentBtn.setOnClickListener {
            if (currentNumber != "") {
                currentNumber = (currentNumber.toDouble() / 100).toString()
                if (currentNumber.contains(".")) {
                    val index = currentNumber.indexOf(".")
                    if (currentNumber[index + 1].toString() == "0" && index + 2 == currentNumber.length) {
                        currentNumber = currentNumber.toDouble().roundToInt().toString()
                    }
                }
                binding.numbersField.text = currentNumber
            }
        }
        binding.backBtn.setOnClickListener {
            if ((currentNumber != "" && currentNumber.length == 1) || (currentNumber.length == 2 && currentNumber[0] == '-')) {
                currentNumber = ""
                binding.numbersField.text = "0"
            } else if (currentNumber != "" && currentNumber.length > 1 && actions.size != 3) {
                currentNumber = currentNumber.dropLast(1)
                binding.numbersField.text = currentNumber
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadOnActionClicked() {
        binding.divideBtn.setOnClickListener {
            clickActionBtn("/")
        }
        binding.multiplyBtn.setOnClickListener {
            clickActionBtn("*")
        }
        binding.plusBtn.setOnClickListener {
            clickActionBtn("+")
        }
        binding.minusBtn.setOnClickListener {
            clickActionBtn("-")
        }
        binding.equalBtn.setOnClickListener {
            if (actions.size == 2 && currentNumber != "") {
                // checking if number ends with .
                if (currentNumber[currentNumber.length - 1].toString() == ".") {
                    currentNumber = currentNumber.dropLast(1)
                }
                actions.add(currentNumber)
                var result = ""
                actions.forEach {
                    result += it
                }
                currentNumber = Expression(result).calculate().toString()
                // checking if result is NaN
                if (currentNumber == "NaN") {
                    currentNumber = ""
                    binding.numbersField.text = "Not a number"
                    actions.clear()
                    return@setOnClickListener
                }
                // checking if result ends with 0
                if (currentNumber.contains(".")) {
                    val index = currentNumber.indexOf(".")
                    if (currentNumber[index + 1] == '0' && index + 2 == currentNumber.length) {
                        currentNumber = currentNumber.toDouble().roundToInt().toString()
                    }
                }
                binding.numbersField.text = currentNumber
            } else if (actions.size == 2) {
                actions.add(actions[0])
                var result = ""
                actions.forEach {
                    result += it
                }
                currentNumber = Expression(result).calculate().toString()
                // checking if result is NaN
                if (currentNumber == "NaN") {
                    currentNumber = ""
                    binding.numbersField.text = "Not a number"
                    actions.clear()
                    return@setOnClickListener
                }
                // checking if result ends with 0
                if (currentNumber.contains(".")) {
                    val index = currentNumber.indexOf(".")
                    if (currentNumber[index + 1] == '0' && index + 2 == currentNumber.length) {
                        currentNumber = currentNumber.toDouble().roundToInt().toString()
                    }
                }
                binding.numbersField.text = currentNumber
                actions[0] = currentNumber
            } else if (actions.size == 3) {
                var result = ""
                for (i in 0 until actions.size) {
                    result += actions[i]
                }
                currentNumber = Expression(result).calculate().toString()
                // checking if result is NaN
                if (currentNumber == "NaN") {
                    currentNumber = ""
                    binding.numbersField.text = "Not a number"
                    actions.clear()
                    return@setOnClickListener
                }
                // checking if result ends with 0
                if (currentNumber.contains(".")) {
                    val index = currentNumber.indexOf(".")
                    if (currentNumber[index + 1] == '0' && index + 2 == currentNumber.length) {
                        currentNumber = currentNumber.toDouble().roundToInt().toString()
                    }
                }
                binding.numbersField.text = currentNumber
                actions[0] = currentNumber
            }
        }
    }

    private fun loadOnNumberClicked() {
        binding.zeroBtn.setOnClickListener {
            clickNumber("0")
        }
        binding.oneBtn.setOnClickListener {
            clickNumber("1")
        }
        binding.twoBtn.setOnClickListener {
            clickNumber("2")
        }
        binding.threeBtn.setOnClickListener {
            clickNumber("3")
        }
        binding.fourBtn.setOnClickListener {
            clickNumber("4")
        }
        binding.fiveBtn.setOnClickListener {
            clickNumber("5")
        }
        binding.sixBtn.setOnClickListener {
            clickNumber("6")
        }
        binding.sevenBtn.setOnClickListener {
            clickNumber("7")
        }
        binding.eightBtn.setOnClickListener {
            clickNumber("8")
        }
        binding.nineBtn.setOnClickListener {
            clickNumber("9")
        }
        binding.pointBtn.setOnClickListener {
            if (!currentNumber.contains(".")) {
                if (currentNumber == "") {
                    clickNumber("0.")
                } else {
                    clickNumber(".")
                }
            }
        }
    }

    private fun clickNumber(number: String) {
        if (currentNumber == "") {
            currentNumber = number
        } else if (currentNumber.lastIndex <= 7 && actions.size != 3) {
            currentNumber += number
        } else if (actions.size == 3) {
            currentNumber = number
            actions[2] = currentNumber
        }
        binding.numbersField.text = currentNumber
    }

    @SuppressLint("SetTextI18n")
    private fun clickActionBtn(actionText: String) {
        if (actions.size == 0 && currentNumber != "") {
            // checking if number ends with .
            if (currentNumber[currentNumber.length - 1] == '.') {
                currentNumber = currentNumber.dropLast(1)
            }
            loosingZeroInTheEnd()
            actions.add(currentNumber)
            actions.add(actionText)
            currentNumber = ""
        } else if (actions.size == 2 && currentNumber == "") {
            actions[1] = actionText
        } else if (actions.size == 2) {
            // checking if number ends with .
            if (currentNumber[currentNumber.length - 1] == '.') {
                currentNumber = currentNumber.dropLast(1)
            }
            actions.add(currentNumber)
            var result = ""
            for (i in 0 until actions.size) {
                result += actions[i]
            }
            currentNumber = Expression(result).calculate().toString()
            // checking if result is NaN
            if (currentNumber == "NaN") {
                currentNumber = ""
                binding.numbersField.text = "Not a number"
                actions.clear()
                return
            }
            // checking if result ends with 0
            if (currentNumber.contains(".")) {
                val index = currentNumber.indexOf(".")
                if (currentNumber[index + 1] == '0' && index + 2 == currentNumber.length) {
                    currentNumber = currentNumber.toDouble().roundToInt().toString()
                }
            }
            binding.numbersField.text = currentNumber
            actions[0] = currentNumber
        } else if (actions.size == 3) {
            actions.clear()
            actions.add(currentNumber)
            actions.add(actionText)
            currentNumber = ""
        }
    }

    private fun loosingZeroInTheEnd() {
        if (currentNumber.contains(".")) {
            for (i in currentNumber.length - 1 downTo 0) {
                if (currentNumber[i] == '0') {
                    currentNumber = currentNumber.dropLast(1)
                } else {
                    break
                }
            }
            binding.numbersField.text = currentNumber
        }
    }
}