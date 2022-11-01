package uz.kosimkhuja.sharipov.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.core.content.ContextCompat
import uz.kosimkhuja.sharipov.calculator.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentNumber = "0"
    private val actions = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // loading actions
        binding.numbersField.text = currentNumber
        loadOnNumberClicked()
        loadOnActionClicked()
        loadOnItemClicked()
    }

    private fun loadOnItemClicked() {
        // adding logic to clear button
        binding.clearBtn.setOnClickListener {
            currentNumber = "0"
            binding.numbersField.text = "0"
            actions.clear()
        }
        // adding logic to positive/negative button
        binding.positiveNegativeBtn.setOnClickListener {
            if (currentNumber != "0") {
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
            if (currentNumber != "0") {
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
            if (currentNumber != "0" && currentNumber.length == 1) {
                currentNumber = "0"
                binding.numbersField.text = currentNumber
            } else if (currentNumber != "0" && currentNumber.length > 1) {
                currentNumber = currentNumber.dropLast(1)
                binding.numbersField.text = currentNumber
            }
        }
    }

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
            if (actions.size == 2 && currentNumber != "0") {
                actions.add(currentNumber)
                var result = ""
                for (i in 0 until actions.size) {
                    result += actions[i]
                }
                currentNumber = Expression(result).calculate().toString()
                if (currentNumber.contains(".")) {
                    val index = currentNumber.indexOf(".")
                    if (currentNumber[index + 1].toString() == "0" && index + 2 == currentNumber.length) {
                        currentNumber = currentNumber.toDouble().roundToInt().toString()
                    }
                }
                binding.numbersField.text = currentNumber
                actions.clear()
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
    }

    private fun clickNumber(number: String) {
        if (currentNumber == "0") {
            currentNumber = number
        } else if (currentNumber != "0" && currentNumber.lastIndex <= 7) {
            currentNumber += number
        }
        binding.numbersField.text = currentNumber
    }

    private fun clickActionBtn(actionText: String) {
        if (actions.size == 0 && currentNumber != "0") {
            actions.add(currentNumber)
            actions.add(actionText)
            currentNumber = "0"
        } else if (actions.size == 2 && currentNumber == "0") {
            actions[1] = actionText
        } else {
            actions.add(currentNumber)
            var result = ""
            for (i in 0 until actions.size) {
                result += actions[i]
            }
            currentNumber = Expression(result).calculate().toString()
            if (currentNumber.contains(".")) {
                val index = currentNumber.indexOf(".")
                if (currentNumber[index + 1].toString() == "0" && index + 2 == currentNumber.length) {
                    currentNumber = currentNumber.toDouble().roundToInt().toString()
                }
            }
            binding.numbersField.text = currentNumber
            actions.clear()
            actions.add(currentNumber)
            actions.add(actionText)
            currentNumber = "0"
        }
    }

}